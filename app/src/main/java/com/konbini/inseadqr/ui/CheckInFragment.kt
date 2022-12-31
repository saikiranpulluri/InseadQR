package com.konbini.inseadqr.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.konbini.inseadqr.R
import com.konbini.inseadqr.databinding.FragmentCheckInBinding
import com.konbini.inseadqr.viewmodels.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CheckInFragment : Fragment() {
    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val customerId = it.getString("customer_id")
            mainViewModel.enableTemporaryLoginForUser(customerId)
        }

        mainViewModel.temporaryLoginURL.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                it?.let { loginURL ->
                    lifecycleScope.launch {
                        delay(100)
                        binding.desc.text = getString(R.string.generating_qr_code_wait_for_print)
                        // Print the QR and show QR on screen
                        // Show the check in complete message
                        // Navigate back
                        delay(100)
                        try {
                            val barcodeEncoder = BarcodeEncoder()
                            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                                loginURL,
                                BarcodeFormat.QR_CODE,
                                500,
                                500
                            )
                            binding.qrImage.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            binding.backButton.visibility = View.VISIBLE
                            binding.qrImage.setImageBitmap(bitmap)
                            binding.desc.text = getString(R.string.check_in_complete)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}