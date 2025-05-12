package com.yuiko.mobile_quiz_system

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div2.DivData
import org.json.JSONObject

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.request_quiz_activity)
//        val button = findViewById<Button>(R.id.quizRequestButton)
//        val quizIdField = findViewById<EditText>(R.id.quizIdField)
//        button.setOnClickListener {
//            val intent = Intent(this, QuizActivity::class.java)
//            intent.putExtra("quizId", quizIdField.text.toString().toLong())
//            startActivity(intent, savedInstanceState)
//        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent, savedInstanceState)
    }
}

fun JSONObject.asDiv2DataWithTemplates(): DivData {
    val templates = getJSONObject("templates")
    val card = getJSONObject("card")
    val environment = DivParsingEnvironment(ParsingErrorLogger.LOG)
    environment.parseTemplates(templates)
    return DivData(environment, card)
}
