package com.yuiko.mobile_quiz_system

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ResultActivity : Activity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        val activityView = findViewById<TextView>(R.id.tvResult)
        val bundle = intent.extras
        val answers = bundle?.getBooleanArray("answersResult") ?: return
        val userId = bundle.getLong("userId")
        val quizId = bundle.getLong("quizId")
        val correct = answers.count { it }
        val total = answers.count()
        if (userId < 1 || quizId < 1) {
            throw IllegalStateException("UserId or QuizId is null")
        }
        sendQuizResult(quizId, userId, correct, total)
        activityView.text = "$correct / $total"

        val backButton = findViewById<Button>(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, AllQuizzesActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }
    }

    private fun sendQuizResult(quizId: Long, userId: Long, correct: Int, total: Int) {
        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("quizId", quizId)
            put("userId", userId)
            put("correctAnswers", correct)
            put("totalQuestions", total)
        }
        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(), json.toString()
        )
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/user/result") // Используй 10.0.2.2 вместо localhost на эмуляторе
            .post(body)
            .build()
        runOnUiThread {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread UI@{
                        Toast.makeText(
                            this@ResultActivity,
                            "Ошибка сети: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(
                                this@ResultActivity,
                                "Отправка результата успешна",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@ResultActivity,
                                "Ошибка во время отправки результата",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        }
    }
}