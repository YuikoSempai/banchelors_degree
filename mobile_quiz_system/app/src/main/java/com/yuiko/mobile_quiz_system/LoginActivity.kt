package com.yuiko.mobile_quiz_system

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoRegister = findViewById<Button>(R.id.btnGoRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            login(username, password)
        }

        btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(username: String, password: String) {
        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        val body = RequestBody.create(
            "application/json".toMediaTypeOrNull(), json.toString()
        )
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/user/auth") // Используй 10.0.2.2 вместо localhost на эмуляторе
            .post(body)
            .build()
        runOnUiThread {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread UI@ {
                        Toast.makeText(
                            this@LoginActivity,
                            "Ошибка сети: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val thread = Thread {
                        if (response.isSuccessful) {
                            val respBody = response.body?.string()
                            val userId: Long?
                            try {
                                userId = respBody?.toLong()
                            } catch (e: Exception) {
                                runOnUiThread {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Ошибка разбора ответа сервера",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@Thread
                            }
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Вход выполнен! id: $userId",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            val intent = Intent(this@LoginActivity, AllQuizzesActivity::class.java)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                            finish()
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Ошибка авторизации",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    thread.start()
                    thread.join()
                }
            })
        }
    }
}
