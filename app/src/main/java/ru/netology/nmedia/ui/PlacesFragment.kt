package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPlacesBinding
import ru.netology.nmedia.ui.viewmodel.MapViewModel

class PlacesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlacesBinding.inflate(inflater, container, false)
        val viewModel by viewModels<MapViewModel>(ownerProducer = ::requireActivity)
        val adapter = PlacesAdapter { viewModel.selectPlace(it) }

        viewModel.places.observe(viewLifecycleOwner, adapter::submitList)

        binding.markersList.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.markers)

//        return FragmentPlacesBinding.inflate(inflater, container, false).root.also {
//            it.adapter = adapter
//            it.recycledViewPool.setMaxRecycledViews(R.layout.fragment_places, 5)
        }
            return binding.root
    }
}

//val binding = FragmentAllMarkersBinding.inflate(inflater, container, false)
//
//val adapter = MarkerAdapter(object : OnInteractionListener{
//
//    override fun onRemove(marker: Marker) {
//        viewModel.removedById(marker.id)
//    }
//
//    override fun onEdit(marker: Marker) {
//        viewModel.edit(marker)
//    }
//})
//binding.markersList.adapter = adapter
//viewModel.data.observe(viewLifecycleOwner, { state ->
//    adapter.submitList(state.markers)
//})
//
//return binding.root