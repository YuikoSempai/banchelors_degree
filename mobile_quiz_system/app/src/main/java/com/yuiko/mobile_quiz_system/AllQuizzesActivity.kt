package com.yuiko.mobile_quiz_system

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.StatusLine
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import com.yuiko.mobile_quiz_system.adapter.TestAdapter
import com.yuiko.mobile_quiz_system.model.QuizzesResponse
import com.yuiko.mobile_quiz_system.model.SimpleQuiz
import java.io.IOException

class AllQuizzesActivity : Activity() {
    private val mapper = jacksonObjectMapper()
    private lateinit var rvTests: RecyclerView
    private lateinit var adapter: TestAdapter
    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_list_activity)
        val bundle = intent.extras
        userId = bundle?.getLong("userId")!!
        rvTests = findViewById(R.id.rvTests)

        adapter = TestAdapter { test ->
//            Toast.makeText(this, "Тест: \"${test.name}\"", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizId", test.id)
            intent.putExtra("userId", userId)
            startActivity(intent, savedInstanceState)
            finish()
            // Здесь можно добавить открытие экрана теста
        }
        rvTests.layoutManager = LinearLayoutManager(this)
        rvTests.adapter = adapter

        initTests()
        initBottomNav()
    }

    private fun initTests() {
        var testList: List<SimpleQuiz> = ArrayList()
        val thread = Thread {
            val url = "http://10.0.2.2:8080/quiz?userId=${userId}"
            val httpclient: HttpClient = DefaultHttpClient()
            val response: HttpResponse = httpclient.execute(HttpGet(url))
            val statusLine: StatusLine = response.getStatusLine()
            if (statusLine.statusCode == HttpStatus.SC_OK) {
                val out: ByteArrayOutputStream = ByteArrayOutputStream()
                response.getEntity().writeTo(out)
                val responseString: String = out.toString()
                val dto: QuizzesResponse = mapper.readValue(responseString)
                out.close()
                testList = dto.result
            } else {
                response.getEntity().getContent().close()
                throw IOException(statusLine.reasonPhrase)
            }
        }
        thread.start()
        thread.join()
//        if (testList.isEmpty()) {
//            throw IllegalStateException("No test elements found")
//        }
        if (testList.isEmpty()) {
            return
        }
        runOnUiThread {
            adapter.setData(testList)
        }
    }

    private fun initBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.quizBottomNav)
        bottomNav.menu.findItem(R.id.menu_tests).isChecked = true
//        bottomNav.menu.findItem(R.id.menu_profile).isChecked = true
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tests -> true
                R.id.menu_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}