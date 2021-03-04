/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Preview
@Composable
fun MyApp() {
    var status = remember({ mutableStateOf("start") })
    var timer: Job? = null
    Surface(color = MaterialTheme.colors.background) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("60s Countdown Timer") },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(R.drawable.dian),
                            contentDescription = "Timer"
                        )
                    }
                )
            },
            floatingActionButton = {
                Button(
                    onClick = {
                        if (status.value == "start") {
                            status.value = "stop"
                            timer = countdownTimer()
                        } else {
                            status.value = "start"
                            timer?.cancel()
                            timeLeftInSeconds.value = 0
                        }
                    }
                ) {
                    Text(
                        text = status.value,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            Surface(color = MaterialTheme.colors.background) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TimerView(timeLeftInSeconds = timeLeftInSeconds)
                    Spacer(modifier = Modifier.size(100.dp))
                    Text(text = "hi")
                }
            }
        }
    }
}
private val timeLeftInSeconds = MutableStateFlow<Long>(0L)

private fun countdownTimer(): Job? {
    var timer: Job?
    val totalSeconds = 60L

    timer = CoroutineScope(Dispatchers.Main).launch {
        for (seconds in totalSeconds downTo 0) {
            val time = String.format(
                "%02d:%02d",
                TimeUnit.SECONDS.toMinutes(seconds),
                seconds - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.SECONDS.toMinutes(
                        seconds
                    )
                )
            )
            timeLeftInSeconds.value = seconds
            delay(1000) // sec
        }
        Log.d(TAG, "Done")
    }
    return timer
}
