package com.example.trackmate.ui.fragments

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.trackmate.R
import com.example.trackmate.ui.viewmodels.MainViewModel
import com.example.trackmate.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run_statistics.*
import kotlinx.android.synthetic.main.fragment_statistics.*

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel : StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLineChart()
        setUpBarChart()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.tracksSortedByDate.observe(viewLifecycleOwner , Observer {
            it?.let {
                val allAvgSpeed = it.indices.map{ i -> BarEntry(i.toFloat() , it[i].avgSpeedInKMH) }
                val startColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
                val endColor = ContextCompat.getColor(requireContext(), R.color.purple_500)

                // Define the gradient object
                val gradient = LinearGradient(0f, 0f, 0f, barChartStatistics.height.toFloat(),
                    startColor, endColor, Shader.TileMode.CLAMP)

                // Create the bar data set and apply the gradient color
                val barDataSet = BarDataSet(allAvgSpeed, "Avg Speed Over Time").apply {
                    valueTextColor = Color.BLACK
                    color = startColor // set the start color to avoid a default color
                    setGradientColor(startColor, endColor)
                }

                barChartStatistics.data = BarData(barDataSet)
                barChartStatistics.apply {
                    animateY(1000)
                    animateX(1000)

                }
                //barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                barChartStatistics.invalidate()
            }
        })

        viewModel.tracksSortedByDate.observe(viewLifecycleOwner , Observer {
            it?.let {
                val allAvgSpeed = it.indices.map{ i -> BarEntry(i.toFloat() , it[i].avgSpeedInKMH) }
                val startColor = ContextCompat.getColor(requireContext(), R.color.purple_200)
                val endColor = ContextCompat.getColor(requireContext(), R.color.purple_700)

                // Define the gradient object
                val gradient = LinearGradient(0f, 0f, 0f, lineChartStatistics.height.toFloat(),
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

                lineChartStatistics.data = LineData(lineDataSet)
                lineChartStatistics.apply {
                    animateY(1000)
                    animateX(1000)

                }
                //barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                lineChartStatistics.invalidate()
            }
        })
    }

    private fun setUpLineChart() {
        lineChartStatistics.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        lineChartStatistics.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        lineChartStatistics.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        lineChartStatistics.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    private fun setUpBarChart() {
        barChartStatistics.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        barChartStatistics.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        barChartStatistics.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(true)
        }
        barChartStatistics.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }
}