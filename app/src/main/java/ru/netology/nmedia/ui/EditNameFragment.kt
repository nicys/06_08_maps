package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentEditNameBinding
import ru.netology.nmedia.ui.util.AndroidUtils
import ru.netology.nmedia.ui.util.StringArg
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel

class EditNameFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditNameBinding.inflate(inflater, container, false)

        binding.newNameOfMarker.requestFocus()
        arguments?.textArg.let(binding.newNameOfMarker::setText)

        binding.cancelButton.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        binding.submitButton.setOnClickListener {
            viewModel.changeNameOfMarker(binding.newNameOfMarker.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        return binding.root
    }

}