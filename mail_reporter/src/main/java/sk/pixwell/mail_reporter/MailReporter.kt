package sk.pixwell.mail_reporter

import com.cookpad.core.Reporter
import com.cookpad.core.ReporterCallback
import com.cookpad.core.models.Report
import javax.activation.FileDataSource
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import android.os.AsyncTask
import java.util.*
import javax.activation.DataHandler
import javax.mail.Message
import javax.mail.Session


class MailReporter(private val subject: String, private val mailTo: String, private val mailFrom: String, val username: String, val password: String,
                   val host: String, val port: Int) : Reporter {



    override fun sendReport(report: Report, reporterCallback: ReporterCallback) {
        val asyncTask = object : AsyncTask<Void, Void, Response>() {
            override fun doInBackground(vararg params: Void?): Response {
                try {


                    val session = createSessionObject()
                    val message = MimeMessage(session)
                    message.setFrom(InternetAddress(mailFrom));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(mailTo))
                    message.setSubject(subject)
                    val text = StringBuilder()
                    text.append("Author ${report.author}\n\nIssue description:e${report.issue}\n\nSteps:\n")
                    report.steps.forEachIndexed { index, it ->

                        text.append("Step ${index + 1} description: ${it.title}\n")
                    }
                    val textBodyPart = MimeBodyPart()
                    textBodyPart.setText(text.toString())


                    val multipart = MimeMultipart()
                    multipart.addBodyPart(textBodyPart)
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