package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.weatherapp.databinding.ActivityHistoryBinding
import java.io.File
import java.io.InputStream

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //DATA FROM FILE
        val inputStream: InputStream = File(filesDir.path + "/logs.csv").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }

        binding.textTemperature.text = inputString

        //CLEAR BUTTON
        binding.clearBtn.setOnClickListener {
            val filename = "logs.csv"
            val fos = openFileOutput(filename, MODE_PRIVATE)
            fos.write("".toByteArray())
            fos.close()

            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

}