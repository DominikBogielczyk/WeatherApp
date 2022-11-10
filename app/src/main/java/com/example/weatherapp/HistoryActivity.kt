package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.weatherapp.databinding.ActivityForecastBinding
import com.example.weatherapp.databinding.ActivityHistoryBinding
import java.io.File
import java.io.InputStream

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //DATA FROM FILE
        val inputStream: InputStream = File("/data/data/com.example.weatherapp/files/logs.csv").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }

        binding.textTemperature.text = inputString

    }
    //CLEAR BUTTON
    fun clear (v: View?)
    {
        val filename = "logs.csv"
        val fos = openFileOutput(filename, MODE_PRIVATE)
        fos.write("".toByteArray())
        fos.close()

        startActivity(Intent(this, HistoryActivity::class.java))
    }
}