package com.yuiko.mobile_quiz_system

import android.app.Activity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.LinearLayout
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.StatusLine
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.view2.Div2View
import com.yandex.div.picasso.PicassoDivImageLoader
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.io.IOException
import java.net.URL


class QuizActivity : Activity() {

    val mapper = jacksonObjectMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_activity)
        val bundle = intent.extras
        val quizId = bundle?.getLong("quizId")
        var quiz: Quiz? = null
        val thread = Thread {
            quiz = getQuiz(quizId)
        }
        thread.start()
        thread.join()
        quiz ?: throw IllegalStateException()
        val imageLoader = PicassoDivImageLoader(applicationContext)
        val configuration = DivConfiguration.Builder(imageLoader).build()
        val card = quiz!!.data
        val divData = JSONObject(card).asDiv2DataWithTemplates()
        val contextThemeWrapper = ContextThemeWrapper(applicationContext, theme)
        val div2View = Div2View(
            Div2Context(
                contextThemeWrapper,
                configuration,
                com.yandex.div.R.style.Div_Theme
            )
        )
        div2View.setData(divData, DivDataTag("divkitLayout"))
        val activityView = findViewById<LinearLayout>(R.id.questionLayout)
        activityView.addView(div2View)

        val button = findViewById<Button>(R.id.finishQuizButton)
        button.setOnClickListener {
            finish()
        }
    }

    private fun getQuiz(quizId: Long?): Quiz? {
        quizId ?: return null
        val url = "http://10.0.2.2:8080/quiz/$quizId"
        val httpclient: HttpClient = DefaultHttpClient()
        val response: HttpResponse = httpclient.execute(HttpGet(url))
        val statusLine: StatusLine = response.getStatusLine()
        if (statusLine.statusCode == HttpStatus.SC_OK) {
            val out: ByteArrayOutputStream = ByteArrayOutputStream()
            response.getEntity().writeTo(out)
            val responseString: String = out.toString()
            val dto: Quiz = mapper.readValue(responseString)
            out.close()
            return dto
        } else {
            response.getEntity().getContent().close()
            throw IOException(statusLine.reasonPhrase)
        }

    }
}