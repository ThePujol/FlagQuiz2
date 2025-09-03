package com.example.flagquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        edtName = findViewById(R.id.edtName)
        btnStart = findViewById(R.id.btnStart)
    }

    private fun setupListeners() {
        btnStart.setOnClickListener {
            startQuiz()
        }
    }

    private fun startQuiz() {
        val playerName = edtName.text.toString().trim()

        if (playerName.isEmpty()) {
            edtName.error = getString(R.string.error_name_required)
            return
        }

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("player_name", playerName)
        startActivity(intent)
    }
}