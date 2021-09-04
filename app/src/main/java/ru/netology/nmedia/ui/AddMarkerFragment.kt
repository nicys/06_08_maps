package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentAddMarkerBinding
import ru.netology.nmedia.ui.MarkersListFragment.Companion.markerData
import ru.netology.nmedia.ui.util.AndroidUtils
import ru.netology.nmedia.ui.viewmodel.MarkerViewModel

class AddMarkerFragment : DialogFragment() {
    private val viewModel: MarkerViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentAddMarkerBinding.inflate(inflater, container, false)

        arguments?.markerData?.let { marker ->
            with(binding) {
                setMarkerTitle.requestFocus()
                AndroidUtils.showKeyboard(requireView())
                setMarkerTitle.setText(marker.title)
                setLatitude.text = marker.latitude.toString()
                setSetLongitude.text = marker.longitude.toString()

                saveButton.setOnClickListener {
                    viewModel.changeData(
                        setMarkerTitle.text.toString(),
                        setLatitude.text.toString().toDouble(),
                        setSetLongitude.text.toString().toDouble()
                    )
                    viewModel.save()
                    AndroidUtils.hideKeyboard(requireView())
                    markerCreatedObserver()
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            val activity = activity ?: return@setOnClickListener
            val dialog = activity.let { activity ->
                AlertDialog.Builder(activity)
            }

            dialog.setMessage(R.string.cancellation)
                .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                    dismiss()
                }
                .setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                    isCancelable
                }
                .create()
                .show()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun markerCreatedObserver() {
        try {
            viewModel.markerCreatedEvent.observe(viewLifecycleOwner, {
                dismiss()
            })
        } catch (e: Exception) {
            e.printStackTrace()
            viewModel.saveMarkerExceptionEvent.observe(viewLifecycleOwner, {
                val activity = activity ?: return@observe
                val dialog = activity.let { activity ->
                    AlertDialog.Builder(activity)
                }
                dialog.setMessage(R.string.error_saving)
                    .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            })
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}