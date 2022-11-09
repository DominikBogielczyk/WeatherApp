package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TempeatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tempeature)

        if (english) {
            findViewById<TextView>(R.id.history_text).text = "Temperature: " + temp + "°C"
            findViewById<TextView>(R.id.feels_like_text).text = "Feels like: " + feels_like + "°C"
            findViewById<TextView>(R.id.temp_min_text).text = "Min. temperature: " + temp_min + "°C"
            findViewById<TextView>(R.id.temp_max_text).text = "Max. temperature: " + temp_max + "°C"
            findViewById<TextView>(R.id.info_text).text = "More info about temperature:"
        } else {
            findViewById<TextView>(R.id.history_text).text = "Temperatura: " + temp + "°C"
            findViewById<TextView>(R.id.feels_like_text).text = "Odczuwalna: " + feels_like + "°C"
            findViewById<TextView>(R.id.temp_min_text).text = "Temperatura min.: " + temp_min + "°C"
            findViewById<TextView>(R.id.temp_max_text).text = "Temperatura max.: " + temp_max + "°C"
            findViewById<TextView>(R.id.info_text).text = "Więcej informacji o temperaturze"
        }
    }
}