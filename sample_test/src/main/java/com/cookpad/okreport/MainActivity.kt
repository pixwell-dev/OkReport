/*
 * Copyright 2017 Cookpad Inc.
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

package com.cookpad.okreport

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btTrigger).setOnClickListener { OkReportApp.shakeGesture.hearShake() }

        shuffleBackground()
    }

    private fun shuffleBackground() {
        var counter = 0
        val images = listOf(R.drawable._1)
        val ivBackground = findViewById<ImageView>(R.id.ivBackground)
        val wait = 2000L
        ivBackground.setImageResource(images[0])

    }
}
