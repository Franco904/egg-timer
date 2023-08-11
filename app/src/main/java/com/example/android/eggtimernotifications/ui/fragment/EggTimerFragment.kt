/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.ui.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.databinding.FragmentEggTimerBinding
import com.example.android.eggtimernotifications.notification.channels.BreakfastNotificationChannel
import com.example.android.eggtimernotifications.notification.channels.EggNotificationChannel
import com.example.android.eggtimernotifications.ui.viewModel.EggTimerViewModel

class EggTimerFragment : Fragment() {
    private val viewModel: EggTimerViewModel by viewModels {
        EggTimerViewModel.provideFactory(application = requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEggTimerBinding.inflate(inflater, container, false).apply {
            eggTimerViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        // Requires Android 8
        createNotificationChannels()

        // Requires Android 13
        requestNotificationPermission()
        setCheckNotificationPermissionListener()

        return binding.root
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val eggNotificationChannel = EggNotificationChannel.create(context = requireActivity())
            val breakfastNotificationChannel = BreakfastNotificationChannel.create(context = requireActivity())

            val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(eggNotificationChannel)
            notificationManager.createNotificationChannel(breakfastNotificationChannel)
        }
    }

    private fun setCheckNotificationPermissionListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.checkNotification.observe(viewLifecycleOwner) {
                val isPermissionGranted = ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED

                if (isPermissionGranted) {
                    viewModel.onNotificationPermissionGranted()
                } else {
                    requestNotificationPermission()
                }
            }
        } else {
            viewModel.onNotificationPermissionGranted()
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0,
            )
        }
    }
}