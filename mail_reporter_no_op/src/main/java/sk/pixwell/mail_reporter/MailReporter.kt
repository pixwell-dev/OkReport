package sk.pixwell.mail_reporter

import android.content.Context
import com.cookpad.core.Reporter
import com.cookpad.core.ReporterCallback
import com.cookpad.core.models.Report

class MailReporter(private val context: Context, private val subject: String, private val mailTo: String, private val mailFrom: String, val username: String, val password: String,
                   val host: String, val port: Int) : Reporter  {
    override fun sendReport(report: Report, reporterCallback: ReporterCallback) {

    }
}