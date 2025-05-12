package com.yuiko.mobile_quiz_system

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : Activity() {

    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        val bundle = intent.extras
        userId = bundle?.getLong("userId")!!
        initBottomNav()
    }

    private fun initBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.profileBottomNav)
        bottomNav.menu.findItem(R.id.menu_profile).isChecked = true
//        bottomNav.menu.findItem(R.id.menu_tests).isChecked = false
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_tests -> {
                    val intent = Intent(this, AllQuizzesActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.menu_profile -> true
                else -> false
            }
        }
    }
}
