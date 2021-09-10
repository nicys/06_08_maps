package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentMarkersListBinding
import ru.netology.nmedia.ui.adapter.MarkerOnInteractionListener
import ru.netology.nmedia.ui.adapter.MarkersAdapter
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.util.CoordinatesArg
import ru.netology.nmedia.ui.util.MarkerArg
import ru.netology.nmedia.ui.util.StringArg
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel

class MarkersListFragment : Fragment() {

    companion object {
        var Bundle.markerData: Marker? by MarkerArg
        var Bundle.coordinatesData: DoubleArray? by CoordinatesArg
    }

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMarkersListBinding.inflate(inflater, container, false)

        val adapter = MarkersAdapter(object : MarkerOnInteractionListener {
            override fun onShowMarker(marker: Marker) {
                viewModel.selected(marker)
                findNavController().navigateUp()
            }



//                findNavController().navigate(
//                    R.id.action_markersListFragment_to_mapsFragment2,
//                    Bundle().apply {
//                        coordinatesData = doubleArrayOf(
//                            marker.latitude,
//                            marker.longitude
//                        )
//                    })
//            }

            override fun onRemove(marker: Marker) {
                viewModel.removeById(marker.id)
            }

            override fun onEdit(marker: Marker) {
                findNavController().navigate(
                    R.id.action_markersListFragment_to_editedMarkerFragment,
                    Bundle().apply {
                        markerData = marker
                        viewModel.edit(marker)
                    }
                )
            }
        })

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launchWhenCreated {
            binding.list.adapter = adapter
            viewModel.data.collectLatest { data ->
                adapter.submitList(data)
            }
        }

        return binding.root
    }
}