package com.example.weatherapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class TemperatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tempeature)

        findViewById<TextView>(R.id.valueTemperature).text = temp.toString() + "°C"
        findViewById<TextView>(R.id.valueFeelsLike).text = feels_like + "°C"
        findViewById<TextView>(R.id.valueTemperatureMin).text = temp_min + "°C"
        findViewById<TextView>(R.id.valueTemperatureMax).text = temp_max + "°C"
    }
}