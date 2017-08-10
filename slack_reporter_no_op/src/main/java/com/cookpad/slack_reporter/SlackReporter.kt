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

package com.cookpad.slack_reporter

import com.cookpad.core.DeviceSpecs
import com.cookpad.core.Reporter
import com.cookpad.core.ReporterCallback
import com.cookpad.core.models.Report

/**
 * A no-op version of SlackReporter that can be used in release builds.
 */
class SlackReporter(val token: String, val webhookURL: String, val deviceSpecs: DeviceSpecs,
                    val idOrNameChannelImages: String, val notifyChannel: Boolean = false) : Reporter {
    override fun sendReport(report: Report, reporterCallback: ReporterCallback) {

    }
}