package com.example.to_do

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var dateTextView: TextView
    private lateinit var phraseTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dateTextView = findViewById(R.id.dateTextView)
        phraseTextView = findViewById(R.id.phraseTextView)

        // Fetch and display the current date
        fetchAndDisplayCurrentDate()

        // Fetch the phrase of the day
        FetchPhraseTask().execute()

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Button click listener to start MainActivity2
        findViewById<Button>(R.id.startButton)?.setOnClickListener {
            val intent = Intent(this, login_signup::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAndDisplayCurrentDate() {
        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate
    }

    @SuppressLint("StaticFieldLeak")
    inner class FetchPhraseTask : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String? {
            val url = URL("https://api.quotable.io/random")
            val connection = url.openConnection() as HttpURLConnection
            return try {
                connection.inputStream.bufferedReader().use {
                    it.readText()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } finally {
                connection.disconnect()
            }
        }

        override fun onPostExecute(result: String?) {
            result?.let {
                val jsonObj = JSONObject(it)
                val content = jsonObj.getString("content")
                phraseTextView.text = content
            }
        }
    }
}
