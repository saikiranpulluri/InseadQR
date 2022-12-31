package com.konbini.inseadqr.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.konbini.inseadqr.R
import com.konbini.inseadqr.databinding.FragmentSettingsBinding
import com.konbini.inseadqr.utils.PrefUtil
import com.konbini.inseadqr.viewmodels.MainViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cloudServerUrl.setText(PrefUtil.getString("AppSettings.Cloud.Host"))
        binding.cloudConsumerKey.setText(PrefUtil.getString("AppSettings.Cloud.ClientId"))
        binding.cloudConsumerSecret.setText(PrefUtil.getString("AppSettings.Cloud.ClientSecret"))

        binding.btnSave.setOnClickListener {
            if (isValidData()) {
                Log.i("SettingsFragment", "Host: ${binding.cloudServerUrl.text.toString()}")
                Log.i("SettingsFragment", "ClientId: ${binding.cloudConsumerKey.text.toString()}")
                Log.i(
                    "SettingsFragment",
                    "ClientSecret: ${binding.cloudConsumerSecret.text.toString()}"
                )
                PrefUtil.setString(
                    "AppSettings.Cloud.Host",
                    binding.cloudServerUrl.text.toString()
                )
                PrefUtil.setString(
                    "AppSettings.Cloud.ClientId",
                    binding.cloudConsumerKey.text.toString()
                )
                PrefUtil.setString(
                    "AppSettings.Cloud.ClientSecret",
                    binding.cloudConsumerSecret.text.toString()
                )

                Toast.makeText(
                    requireContext(),
                    getString(R.string.settings_saved),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidData(): Boolean {
        if (binding.cloudServerUrl.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.server_url_empty),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.cloudConsumerKey.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.client_id_empty),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.cloudConsumerSecret.text.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.client_secret_empty),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}