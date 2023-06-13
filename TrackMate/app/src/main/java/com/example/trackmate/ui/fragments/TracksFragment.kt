package com.example.trackmate.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackmate.R
import com.example.trackmate.adapters.TrackAdapter
import com.example.trackmate.others.SortType
import com.example.trackmate.ui.bottomsheets.BottomSheetsFragment
import com.example.trackmate.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_start.*
import kotlinx.android.synthetic.main.fragment_tracks.*

@AndroidEntryPoint
class TracksFragment : Fragment(R.layout.fragment_tracks) {
    private val viewModel : MainViewModel by viewModels()
    private lateinit var trackAdapter: TrackAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        trackAdapter.onItemClick = { selectedTrack->
            val bundle = Bundle().apply {
                putParcelable("selectedTrack", selectedTrack)
            }
            findNavController().navigate(
                R.id.action_tracksFragment_to_detailsFragment,
                bundle
            )
        }

        viewModel.tracks.observe(viewLifecycleOwner , Observer {
            trackAdapter.submitList(it)
        })

        viewModel.data.observe(viewLifecycleOwner, Observer { data ->
            when(data) {
                0 -> viewModel.sortRuns(SortType.DATE)
                1 -> viewModel.sortRuns(SortType.TRACKING_TIME)
                2 -> viewModel.sortRuns(SortType.DISTANCE)
                3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                4 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
            }
        })

        when(viewModel.sortType) {
            SortType.DATE -> spFilter.setSelection(0)
            SortType.TRACKING_TIME -> spFilter.setSelection(1)
            SortType.DISTANCE -> spFilter.setSelection(2)
            SortType.AVG_SPEED -> spFilter.setSelection(3)
            SortType.CALORIES_BURNED -> spFilter.setSelection(4)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> viewModel.sortRuns(SortType.DATE)
                    1 -> viewModel.sortRuns(SortType.TRACKING_TIME)
                    2 -> viewModel.sortRuns(SortType.DISTANCE)
                    3 -> viewModel.sortRuns(SortType.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortType.CALORIES_BURNED)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

//        val bottomSheetsFragment = BottomSheetsFragment()
//        sortBtn.setOnClickListener {
//            bottomSheetsFragment.show(childFragmentManager, "BottomSheetDialog")
//        }
    }

    private fun setupRecyclerView() = rvTracks.apply {
        trackAdapter = TrackAdapter()
        adapter = trackAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}
