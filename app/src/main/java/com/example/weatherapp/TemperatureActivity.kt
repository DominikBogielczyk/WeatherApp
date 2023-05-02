package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.weatherapp.databinding.ActivityTemperatureBinding

class TemperatureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTemperatureBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityTemperatureBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.valueTemperature.text = getString(R.string.tempValue, temp.toString())
        binding.valueFeelsLike.text = getString(R.string.tempValue, feels_like)
        binding.valueTemperatureMin.text = getString(R.string.tempValue, temp_min)
        binding.valueTemperatureMax.text = getString(R.string.tempValue, temp_max)
    }
}