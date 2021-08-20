package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentAllMarkersBinding
import ru.netology.nmedia.ui.adapter.MarkerAdapter
import ru.netology.nmedia.ui.adapter.OnInteractionListener
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel

class AllMarkersFragment : Fragment() {

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAllMarkersBinding.inflate(inflater, container, false)

        val adapter = MarkerAdapter(object : OnInteractionListener{

            override fun onRemove(marker: Marker) {
                viewModel.removedById(marker.id)
            }

            override fun onEdit(marker: Marker) {
                viewModel.edit(marker)
            }
        })
//        viewModel.places.observe(viewLifecycleOwner, adapter::submitList)
        binding.markersList.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.markers)
        })

//        return FragmentAllMarkersBinding.inflate(inflater, container, false).root.also {
//            it.adapter = adapter
//            it.recycledViewPool.setMaxRecycledViews(R.layout.item_place, 5)
//        }

        return binding.root
    }
}