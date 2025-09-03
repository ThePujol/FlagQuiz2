package com.example.flagquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var txtPlayer: TextView
    private lateinit var txtScore: TextView
    private lateinit var summaryContainer: LinearLayout
    private lateinit var btnPlayAgain: Button

    private var playerName: String? = null
    private var score: Int = 0
    private var summary: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initViews()
        getIntentData()
        displayResults()
        setupListeners()
    }

    private fun initViews() {
        txtPlayer = findViewById(R.id.txtPlayer)
        txtScore = findViewById(R.id.txtScore)
        summaryContainer = findViewById(R.id.summaryContainer)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
    }

    private fun getIntentData() {
        playerName = intent.getStringExtra("player_name")
        score = intent.getIntExtra("score", 0)
        summary = intent.getStringArrayListExtra("summary")
    }

    private fun displayResults() {
        // Display player greeting
        val greeting = if (playerName.isNullOrBlank()) {
            getString(R.string.congratulations_generic)
        } else {
            getString(R.string.congratulations_player, playerName)
        }
        txtPlayer.text = greeting

        // Display final score
        txtScore.text = getString(R.string.final_score, score)

        // Display summary lines
        summary?.forEach { summaryLine ->
            val textView = TextView(this)
            textView.text = summaryLine
            textView.textSize = 16f
            textView.setPadding(0, 8, 0, 8)
            summaryContainer.addView(textView)
        }
    }

    private fun setupListeners() {
        btnPlayAgain.setOnClickListener {
            playAgain()
        }
    }

    private fun playAgain() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}