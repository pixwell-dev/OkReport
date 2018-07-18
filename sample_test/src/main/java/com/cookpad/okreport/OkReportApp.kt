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
 */package com.cookpad.okreport

import android.app.Application

import com.cookpad.core.initOkReport
import com.cookpad.shake_gesture.ShakeGesture

import sk.pixwell.mail_reporter.MailReporter

class OkReportApp : Application() {
    companion object {
        lateinit var shakeGesture: ShakeGesture
    }

    override fun onCreate() {
        super.onCreate()
        shakeGesture = ShakeGesture(this)

        val mailReporter = MailReporter("New bug report from ${getString(R.string.app_name)}",
                "email@email.com",
                "email@pixwell.sk",
                "b1435dbc109a69a5f77e1cb05c72c63d",
                "cbed8dbb5b90aef234b356bf893420f1",
                "smtp.emailserver.com", 587);
        initOkReport(this, shakeGesture, mailReporter)
    }
}
