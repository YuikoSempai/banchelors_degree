package com.yuiko.mobile_quiz_system

import android.app.Activity
import android.content.Intent
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
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.Variable
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yuiko.mobile_quiz_system.model.Quiz
import org.json.JSONObject
import java.io.IOException


class QuizActivity : Activity() {

    private val mapper = jacksonObjectMapper()

    private lateinit var configuration: DivConfiguration

    private val answerResults = HashMap<Long, Boolean>()

    private lateinit var quiz: Quiz;

    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        val quizId = bundle?.getLong("quizId") ?: return
        userId = bundle.getLong("userId")
        if (userId < 1) {
            throw IllegalStateException("UserId is lower than zero")
        }
        val pageId = 1L
        initActivity()
        initDivKit()
        quiz = initQuiz(quizId, pageId)
    }

    private fun initActivity() {
        setContentView(R.layout.test_quiz_activity)
        val closeButton = findViewById<Button>(R.id.btnExit)
        closeButton.setOnClickListener {
            val intent = Intent(this, AllQuizzesActivity::class.java)
            intent.putExtra("quizId", quiz.id)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }

        val nextButton = findViewById<Button>(R.id.btnNext)
        nextButton.setOnClickListener {
            getAnswerValue()
            if (quiz.hasNextPage) {
                quiz = initQuiz(quiz.id, quiz.page + 1)
            } else {
                if (userId == null) {
                    throw IllegalStateException("User id is null")
                }
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("answersResult", answerResults.values.toBooleanArray())
                intent.putExtra("userId", userId)
                intent.putExtra("quizId", quiz.id)
                startActivity(intent)
                finish()
            }
        }
        val prevButton = findViewById<Button>(R.id.btnPrevious)
        prevButton.setOnClickListener {
            if (quiz.page > 1) {
                quiz = initQuiz(quiz.id, quiz.page - 1)
            }
        }
    }

    private fun initDivKit() {
        val imageLoader = PicassoDivImageLoader(applicationContext)
        configuration = DivConfiguration.Builder(imageLoader)
            .divVariableController(DivVariableController())
            .build()
    }

    private fun initQuiz(quizId: Long, pageId: Long): Quiz {
        var currentQuiz: Quiz? = null
        val thread = Thread {
            currentQuiz = getQuiz(quizId, pageId)
        }
        thread.start()
        thread.join()
        currentQuiz ?: throw IllegalStateException()
        val activityView = findViewById<LinearLayout>(R.id.layoutAnswerContainer)
        activityView.removeAllViews()

        val card = currentQuiz!!.divKitData
        val divData = JSONObject(card).asDiv2DataWithTemplates()
        val contextThemeWrapper = ContextThemeWrapper(applicationContext, theme)
        configuration.divVariableController.putOrUpdate(
            Variable.BooleanVariable(
                "is_correct",
                false
            )
        )

        val div2View = Div2View(
            Div2Context(
                contextThemeWrapper,
                configuration,
                com.yandex.div.R.style.Div_Theme
            )
        )
        div2View.setData(divData, DivDataTag("divkitLayout"))

        activityView.addView(div2View)
        return currentQuiz!!
    }

    private fun getQuiz(quizId: Long?, pageId: Long): Quiz? {
        quizId ?: return null
        val url = "http://10.0.2.2:8080/quiz/$quizId?pageId=${pageId}"
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

    private fun getAnswerValue(): Boolean {
        val variableController = configuration.divVariableController
        val value = variableController.get("is_correct")?.getValue() as? Boolean ?: false
        answerResults[quiz.page] = value
        return value
    }
}