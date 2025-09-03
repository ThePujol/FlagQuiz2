package com.example.flagquiz

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.Normalizer
import java.util.*

class QuizActivity : AppCompatActivity() {

    private lateinit var txtProgress: TextView
    private lateinit var imgFlag: ImageView
    private lateinit var edtAnswer: EditText
    private lateinit var txtFeedback: TextView
    private lateinit var btnAnswer: Button
    private lateinit var btnNext: Button

    private var playerName: String? = null
    private var currentIndex = 0
    private var score = 0
    private var currentFive = mutableListOf<String>()
    private var summary = ArrayList<String>()

    // Pool of country identifiers matching drawable resource names
    private val flagPool = listOf(
        "brasil", "japao", "alemanha", "franca", "italia", "espanha",
        "argentina", "mexico", "canada", "australia", "china", "india",
        "reino_unido", "coreia_sul", "russia"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        initViews()
        getIntentData()

        if (savedInstanceState == null) {
            initializeQuiz()
        } else {
            restoreState(savedInstanceState)
        }

        setupListeners()
        renderCurrentQuestion()
    }

    private fun initViews() {
        txtProgress = findViewById(R.id.txtProgress)
        imgFlag = findViewById(R.id.imgFlag)
        edtAnswer = findViewById(R.id.edtAnswer)
        txtFeedback = findViewById(R.id.txtFeedback)
        btnAnswer = findViewById(R.id.btnAnswer)
        btnNext = findViewById(R.id.btnNext)
    }

    private fun getIntentData() {
        playerName = intent.getStringExtra("player_name")
    }

    private fun initializeQuiz() {
        currentFive = flagPool.shuffled().take(5).toMutableList()
        currentIndex = 0
        score = 0
        summary.clear()
    }

    private fun setupListeners() {
        btnAnswer.setOnClickListener {
            checkAnswer()
        }

        btnNext.setOnClickListener {
            nextQuestion()
        }

        edtAnswer.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                if (btnAnswer.isEnabled) {
                    checkAnswer()
                }
                true
            } else {
                false
            }
        }
    }

    private fun renderCurrentQuestion() {
        val questionNumber = currentIndex + 1
        txtProgress.text = getString(R.string.progress_format, questionNumber)

        val currentCountry = currentFive[currentIndex]
        val resourceId = resources.getIdentifier("flag_$currentCountry", "drawable", packageName)

        if (resourceId != 0) {
            imgFlag.setImageResource(resourceId)
            imgFlag.contentDescription = getString(R.string.flag_content_description, getCapitalizedCountryName(currentCountry))
        } else {
            Toast.makeText(this, getString(R.string.flag_not_found, "flag_$currentCountry"), Toast.LENGTH_SHORT).show()
            // Set a placeholder or keep previous image
        }

        // Reset UI state for new question
        edtAnswer.text.clear()
        edtAnswer.isEnabled = true
        txtFeedback.text = ""
        btnAnswer.isEnabled = true
        btnNext.isEnabled = false
    }

    private fun checkAnswer() {
        val userAnswer = edtAnswer.text.toString().trim()
        val currentCountry = currentFive[currentIndex]
        val questionNumber = currentIndex + 1

        if (userAnswer.isBlank()) {
            // Blank answer is incorrect
            txtFeedback.text = getString(R.string.incorrect_answer, getCapitalizedCountryName(currentCountry))
            summary.add(getString(R.string.summary_incorrect, questionNumber, currentCountry))
        } else {
            val normalizedAnswer = normalize(userAnswer)
            val normalizedCountry = normalize(currentCountry)

            if (normalizedAnswer == normalizedCountry) {
                // Correct answer
                score += 20
                txtFeedback.text = getString(R.string.correct_answer)
                summary.add(getString(R.string.summary_correct, questionNumber, currentCountry))
            } else {
                // Incorrect answer
                txtFeedback.text = getString(R.string.incorrect_answer, getCapitalizedCountryName(currentCountry))
                summary.add(getString(R.string.summary_incorrect, questionNumber, currentCountry))
            }
        }

        // Disable answer input and button, enable next
        edtAnswer.isEnabled = false
        btnAnswer.isEnabled = false
        btnNext.isEnabled = true
    }

    private fun nextQuestion() {
        currentIndex++

        if (currentIndex < 5) {
            // More questions remaining
            renderCurrentQuestion()
        } else {
            // Quiz finished, go to results
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("player_name", playerName)
            intent.putExtra("score", score)
            intent.putStringArrayListExtra("summary", summary)
            startActivity(intent)
            finish() // Avoid back-stack loops
        }
    }

    private fun normalize(input: String): String {
        // Remove diacritics using NFD normalization
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        val withoutDiacritics = normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        // Trim, lowercase, and collapse multiple spaces
        return withoutDiacritics.trim().lowercase(Locale.getDefault()).replace("\\s+".toRegex(), " ")
    }

    private fun getCapitalizedCountryName(country: String): String {
        return country.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentIndex", currentIndex)
        outState.putInt("score", score)
        outState.putStringArrayList("currentFive", ArrayList(currentFive))
        outState.putStringArrayList("summary", summary)
        outState.putString("currentAnswer", edtAnswer.text.toString())
        outState.putString("currentFeedback", txtFeedback.text.toString())
        outState.putBoolean("answerEnabled", btnAnswer.isEnabled)
        outState.putBoolean("nextEnabled", btnNext.isEnabled)
        outState.putBoolean("editEnabled", edtAnswer.isEnabled)
    }

    private fun restoreState(savedInstanceState: Bundle) {
        currentIndex = savedInstanceState.getInt("currentIndex", 0)
        score = savedInstanceState.getInt("score", 0)
        currentFive = savedInstanceState.getStringArrayList("currentFive")?.toMutableList() ?: mutableListOf()
        summary = savedInstanceState.getStringArrayList("summary") ?: ArrayList()

        // Restore UI state after views are initialized
        edtAnswer.post {
            edtAnswer.setText(savedInstanceState.getString("currentAnswer", ""))
            txtFeedback.text = savedInstanceState.getString("currentFeedback", "")
            btnAnswer.isEnabled = savedInstanceState.getBoolean("answerEnabled", true)
            btnNext.isEnabled = savedInstanceState.getBoolean("nextEnabled", false)
            edtAnswer.isEnabled = savedInstanceState.getBoolean("editEnabled", true)
        }
    }
}