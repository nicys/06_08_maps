package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentEditedMarkerBinding
import ru.netology.nmedia.ui.dto.Marker
import ru.netology.nmedia.ui.util.AndroidUtils.hideKeyboard
import ru.netology.nmedia.ui.util.AndroidUtils.showKeyboard
import ru.netology.nmedia.ui.util.CoordinatesArg
import ru.netology.nmedia.ui.util.MarkerArg
import ru.netology.nmedia.ui.util.StringArg
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel
import java.util.*

class EditedMarkerFragment : Fragment() {
    companion object {
        var Bundle.markerData: Marker? by MarkerArg
    }

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEditedMarkerBinding.inflate(inflater, container, false)

        binding.editMarker.requestFocus()
        showKeyboard(binding.root)

        arguments?.markerData?.let {
            with(binding) {
                editMarker.setText(it.title)
                save.setOnClickListener {
                    val editedTitle = editMarker.text.toString().trim()
                    viewModel.changeTitle(editedTitle)
                    viewModel.save()
                    hideKeyboard(requireView())
                    findNavController().navigateUp()
                }
            }

        }

        binding.cancel.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}