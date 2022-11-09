package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.io.InputStream

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val clear_button: Button = findViewById(R.id.clear_btn)
        val t: TextView = findViewById(R.id.head_text)
        if (!english) {
            clear_button.text = "Wyczyść"
            t.text = "HISTORIA"
        }
        else
        {
            clear_button.text = "CLear"
            t.text = "HISTORY"
        }

        findViewById<TextView>(R.id.history_text).text = "TEST"

        //DATA FROM FILE
        val inputStream: InputStream = File("/data/data/com.example.weatherapp/files/logs.csv").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }

        findViewById<TextView>(R.id.history_text).text = inputString

        clear_button.setOnClickListener {
            val FILE_NAME = "logs.csv"
            val fos = openFileOutput(FILE_NAME, MODE_PRIVATE)
            fos.write("".toByteArray())
            fos.close()

            startActivity(Intent(this, HistoryActivity::class.java))
        }

    }
}