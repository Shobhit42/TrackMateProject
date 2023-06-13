package com.example.trackmate.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.trackmate.R
import com.example.trackmate.others.TrackingUtility
import com.example.trackmate.ui.viewmodels.MainViewModel
import com.example.trackmate.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_walk_statistic.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class WalkStatisticsFragment : Fragment(R.layout.fragment_walk_statistic) {

    private val viewModel : StatisticsViewModel by viewModels()

    private val swapRotate: Animation by lazy {
        AnimationUtils.loadAnimation(activity , R.anim.rotate_swap_btn);
    }

    private var clicked = false;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swapGraphWalkingStatistics.setOnClickListener{
            val mediaPlayer = MediaPlayer.create(
                context,
                R.raw.swip)
            mediaPlayer.start()
            swapGraphWalkingStatistics.startAnimation(swapRotate)
            if(!clicked){
                walkStatisticsBarChart.visibility = View.GONE
                walkStatisticsLineChart.visibility = View.VISIBLE
                setUpLineChart()
                subscribeToObservers()
                clicked = true
            }else{
                walkStatisticsBarChart.visibility = View.VISIBLE
                walkStatisticsLineChart.visibility = View.GONE
                setUpBarChart()
                subscribeToObservers()
                clicked = false
            }
        }
        subscribeToObservers()
        setUpBarChart()
        setUpLineChart()
    }

    private fun setUpLineChart() {
        walkStatisticsLineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        walkStatisticsLineChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        walkStatisticsLineChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        walkStatisticsLineChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    private fun setUpBarChart() {
        walkStatisticsBarChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        walkStatisticsBarChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        walkStatisticsBarChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        walkStatisticsBarChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeToObservers(){
        viewModel.getTotalTimeInMillisWalk.observe(viewLifecycleOwner , Observer {
            it?.let {
                val totalTimeWalk = TrackingUtility.getFormattedShopWatchTime(it)
                totalTimeWaking.text = totalTimeWalk
            }
        })

        viewModel.getTotalDistanceWalk.observe(viewLifecycleOwner , Observer {
            it?.let {
                val km = it/1000
                val totalDistanceWalk = round(km * 10f)/10f
                val totalDistanceWalkString = "${totalDistanceWalk}Km"
                totalDistanceCoveredWalking.text = totalDistanceWalkString
            }
        })

        viewModel.getTotalCaloriesBurnedWalk.observe(viewLifecycleOwner , Observer {
            it?.let{
                val totalCaloriesWalkString = "${it}Kcal"
                totalCaloriesBurnedWalking.text = totalCaloriesWalkString
            }
        })

        val calendar = Calendar.getInstance()
        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        lifecycleScope.launch {
            val totalCaloriesBurnedLastWeek = viewModel.getTotalCaloriesBurnedWalkOnWeekNumber(weekNumber-1)
            val totalCaloriesBurnedThisWeek = viewModel.getTotalCaloriesBurnedWalkOnWeekNumber(weekNumber)
            if(totalCaloriesBurnedThisWeek != null && totalCaloriesBurnedLastWeek != null) {
                val diff = totalCaloriesBurnedThisWeek - totalCaloriesBurnedLastWeek
                calorieBurnedStatusWalking.text = if(diff >= 0) "+${diff}Kcal" else "-${diff}Kcal"
            }
        }

        viewModel.getWalkingTracksSortedByDate.observe(viewLifecycleOwner , Observer {
            it?.let {
                val allAvgSpeed = it.indices.map{ i -> BarEntry(i.toFloat() , it[i].avgSpeedInKMH) }
                val startColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
                val endColor = ContextCompat.getColor(requireContext(), R.color.purple_500)

                // Define the gradient object
                val gradient = LinearGradient(0f, 0f, 0f, walkStatisticsBarChart.height.toFloat(),
                    startColor, endColor, Shader.TileMode.CLAMP)

                // Create the bar data set and apply the gradient color
                val barDataSet = BarDataSet(allAvgSpeed, "Avg Speed Over Time").apply {
                    valueTextColor = Color.BLACK
                    color = startColor // set the start color to avoid a default color
                    setGradientColor(startColor, endColor)
                }

                walkStatisticsBarChart.data = BarData(barDataSet)
                walkStatisticsBarChart.apply {
                    animateY(1000)
                    animateX(1000)

                }
                //barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                walkStatisticsBarChart.invalidate()
            }
        })

        viewModel.getWalkingTracksSortedByDate.observe(viewLifecycleOwner , Observer {
            it?.let {
                val allAvgSpeed = it.indices.map{ i -> BarEntry(i.toFloat() , it[i].avgSpeedInKMH) }
                val startColor = ContextCompat.getColor(requireContext(), R.color.purple_700)
                val endColor = ContextCompat.getColor(requireContext(), R.color.purple_700)

                // Define the gradient object
                val gradient = LinearGradient(0f, 0f, 0f, walkStatisticsBarChart.height.toFloat(),
                    startColor, endColor, Shader.TileMode.CLAMP)

                // Create the bar data set and apply the gradient color
                val lineDataSet = LineDataSet(allAvgSpeed, "Avg Speed Over Time").apply {
//                    valueTextColor = Color.BLACK
//                    color = startColor // set the start color to avoid a default color
//                    setGradientColor(startColor, endColor)
                    valueTextColor = Color.BLACK
                    color = startColor // set the start color to avoid a default color
                    setGradientColor(startColor, endColor)
                    setDrawFilled(true)
                    fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.line_chart_gradient)
                    mode = LineDataSet.Mode.CUBIC_BEZIER // for curved lines
                    cubicIntensity = 0.2f // adjust the curve intensity as needed
                }

                walkStatisticsLineChart.data = LineData(lineDataSet)
                walkStatisticsLineChart.apply {
                    animateY(1000)
                    animateX(1000)

                }
                //barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                walkStatisticsLineChart.invalidate()
            }
        })
    }
}
