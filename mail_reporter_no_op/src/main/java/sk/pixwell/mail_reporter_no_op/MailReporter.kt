package sk.pixwell.mail_reporter_no_op

import com.cookpad.core.Reporter
import com.cookpad.core.ReporterCallback
import com.cookpad.core.models.Report

class MailReporter(private val subject: String, private val mailTo: String, private val mailFrom: String, val username: String, val password: String,
                   val host: String, val port: Int) : Reporter  {
    override fun sendReport(report: Report, reporterCallback: ReporterCallback) {

    }
}