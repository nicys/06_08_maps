package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

class EditedMarkerFragment : Fragment() {
    companion object {
        var Bundle.stringData: String? by StringArg
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

        val binding = FragmentEditedMarkerBinding.inflate(inflater, container, false)
//        arguments?.editArg?.let { list ->
//            with(binding) {
//                helpText.text = list.first()
//                descriptionText.setText(list.last())
//            }
//        }

        binding.editMarker.requestFocus()
        showKeyboard(binding.root)

        arguments?.markerData?.let {
            binding.save.setOnClickListener {
//                when (binding.helpText.text) {
//                    getString(R.string.new_marker) ->
                        viewModel.changeTitle(R.id.set_marker_title.toString())
//                    getString(R.string.latitude) ->
//                        viewModel.changeLatitude(R.id.description_text.toString().toDouble())
//                    getString(R.string.longitude) ->
//                        viewModel.changeLongitude(R.id.description_text.toString().toDouble())
//                }
                viewModel.save()
                hideKeyboard(requireView())
//                dismiss()
                findNavController().navigateUp()
            }
        }

        binding.cancel.setOnClickListener {
            findNavController().navigateUp()

//            val activity = activity ?: return@setOnClickListener
//            val dialog = activity.let { activity ->
//                AlertDialog.Builder(activity)
//            }
//
//            dialog.setMessage(R.string.cancellation)
//                .setPositiveButton(R.string.dialog_positive_button) { _, _ ->
////                    dismiss()
//                    hideKeyboard(requireView())
//                    findNavController().navigateUp()
//                }
//                .setNegativeButton(R.string.dialog_negative_button) { _, _ ->
//                    isCancelable
//                }
//                .create()
//                .show()
        }

        return binding.root
    }

//    override fun onStart() {
//        super.onStart()
//        val width = (resources.displayMetrics.widthPixels)
//        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
//    }
}