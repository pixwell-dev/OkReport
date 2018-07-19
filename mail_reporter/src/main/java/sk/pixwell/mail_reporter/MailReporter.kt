package sk.pixwell.mail_reporter

import android.content.Context
import com.cookpad.core.models.Report
import javax.activation.FileDataSource
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import android.os.AsyncTask
import com.cookpad.core.*
import java.text.SimpleDateFormat
import java.util.*
import javax.activation.DataHandler
import javax.mail.BodyPart
import javax.mail.Message
import javax.mail.Session


class MailReporter(private val context: Context, private val subject: String, private val mailTo: String, private val mailFrom: String, val username: String, val password: String,
                   val host: String, val port: Int) : Reporter {



    override fun sendReport(report: Report, reporterCallback: ReporterCallback) {
        val asyncTask = object : AsyncTask<Void, Void, Response>() {
            override fun doInBackground(vararg params: Void?): Response {
                try {


                    val session = createSessionObject()
                    var message = MimeMessage(session)

                    message = buildMessage(message, report)


                    Transport.send(message)

                    return Success("Your email was sent")
                } catch (e: Exception) {
                    return Failure(e)
                }

            }

            override fun onPostExecute(response: Response) {
                when (response) {
                    is Success -> reporterCallback.success(response.message)
                    is Failure -> reporterCallback.error(response.exception)
                }
            }

        }
        asyncTask.execute(null)

    }

    private fun buildMessage(message: MimeMessage, report: Report): MimeMessage {
        message.setFrom(InternetAddress(mailFrom));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(mailTo))
        message.setSubject(subject)



        val multipart = MimeMultipart()
        multipart.addBodyPart(buildTextMessage(report))

        report.steps.forEachIndexed { index, it ->
            val messageBodyPart = MimeBodyPart()

            val file = it.pathImage
            val fileName = "Screenshot ${index + 1}"
            val source = FileDataSource(file)
            messageBodyPart.setDataHandler(DataHandler(source))
            messageBodyPart.setFileName(fileName)
            multipart.addBodyPart(messageBodyPart)
        }


        message.setContent(multipart)
        return message
    }

    private fun buildTextMessage(report: Report): BodyPart? {
        val text = StringBuilder()


        text.append("Date and time: ${getCurrentTimeStamp()}\n")
        text.append("Author ${report.author}\n\nIssue description:e${report.issue}\n\nSteps:\n")
        report.steps.forEachIndexed { index, it ->

            text.append("Step ${index + 1} description: ${it.title}\n")
        }

        val deviceSpecs = collectDeviceSpecs(context)
        text.append("Device make: ${deviceSpecs.deviceMake}\n")
        text.append("Device Model: ${deviceSpecs.deviceModel}\n")
        text.append("Device Resolution: ${deviceSpecs.deviceResolution}\n")
        text.append("Device Density: ${deviceSpecs.deviceDensity}\n")
        text.append("Version Code: ${deviceSpecs.versionCode}\n")
        text.append("Version Release: ${deviceSpecs.versionRelease}\n")
        text.append("Android Version: ${deviceSpecs.locale}\n")

        val networkSpecs = collectNetworkSpecs(context)
        text.append("Network available: ${networkSpecs.available}\n")
        text.append("Network connected: ${networkSpecs.connected}\n")
        text.append("Network failover: ${networkSpecs.failover}\n")
        text.append("Network roaming: ${networkSpecs.roaming}\n")
        text.append("Network type: ${networkSpecs.typeName}\n")
        text.append("Network subtype: ${networkSpecs.subtypeName}\n")

        val textBodyPart = MimeBodyPart()
        textBodyPart.setText(text.toString())
        return textBodyPart;
    }

    fun getCurrentTimeStamp(): String {
        val sdfDate = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY)//dd/MM/yyyy
        val now = Date()
        return sdfDate.format(now)
    }


    private fun createSessionObject(): Session? {
        val properties = Properties()
        properties.put("mail.smtp.auth", "true")
        properties.put("mail.smtp.starttls.enable", "true")
        properties.put("mail.smtp.host", host)
        properties.put("mail.smtp.port", port)


        return Session.getInstance(properties, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                return javax.mail.PasswordAuthentication(username, password)
            }
        })

    }


}

private sealed class Response()
private data class Success(val message: String) : Response()
private data class Failure(val exception: Throwable) : Response()