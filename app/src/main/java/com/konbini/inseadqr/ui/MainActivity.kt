package com.konbini.inseadqr.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.konbini.inseadqr.R
import com.konbini.inseadqr.utils.AppSettings
import com.konbini.inseadqr.utils.PrefUtil
import com.konbini.inseadqr.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PrefUtil.setString(
            "AppSettings.Cloud.Host",
            AppSettings.Cloud.Host
        )
        PrefUtil.setString(
            "AppSettings.Cloud.ClientId",
            AppSettings.Cloud.ClientId
        )
        PrefUtil.setString(
            "AppSettings.Cloud.ClientSecret",
            AppSettings.Cloud.ClientSecret
        )
        setContentView(R.layout.activity_main)

    }
}