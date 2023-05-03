package com.example.weatherapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityForecastBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

const val size = 10
var clouds_v = Array(size) { 0 }
var pop = Array(size) { 0 }
var temperatures = Array(size) { 0 }
var data_axis = Array(size) { "" }
var x = Array(size) { "" }
var icons = Array(size) { "" }

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ReadForecast().execute()

        //TEMPERATURE GRAPH
        binding.tempGraphBtn.setOnClickListener {
            temp_graph = true
            pop_graph = false
            clouds_graph = false
            startActivity(Intent(this, GraphActivity::class.java))
        }
        //POP GRAPH
        binding.popGraphBtn.setOnClickListener {
            temp_graph = false
            pop_graph = true
            clouds_graph = false
            startActivity(Intent(this, GraphActivity::class.java))
        }
        //CLOUDS GRAPH
        binding.cloudsGraphBtn.setOnClickListener {
            temp_graph = false
            pop_graph = false
            clouds_graph = true
            startActivity(Intent(this, GraphActivity::class.java))
        }
    }

    inner class ReadForecast : CoroutineScope {

        private val job = Job()

        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.IO

        fun execute() = launch {
            val result = readData()
            withContext(Dispatchers.Main) {
                if (result != "")
                    handleData(result)
            }
        }

        private suspend fun readData(): String {
            var forecastData = ""
            try {
                forecastData = URL("https://api.openweathermap.org/data/2.5/forecast?q=$location&units=metric&cnt=$cnt&appid=$key").readText(Charsets.UTF_8)
            } catch (e: Exception) {
                // change to main UI thread
                withContext(Dispatchers.Main) {
                    val wifi = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                    if (!wifi.isWifiEnabled) {
                        Toast.makeText(
                            this@ForecastActivity,
                            getString(R.string.internet_error),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ForecastActivity,
                            getString(R.string.input_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            return forecastData
        }

        private fun handleData(result: String) {
            try {
                val jsonObj = JSONObject(result)
                var forecastText = ""

                for (i in 0 until cnt) {
                    val record = jsonObj.getJSONArray("list").getJSONObject(i)
                    pop[i] = (100 * record.getDouble("pop")).toInt()

                    val main = record.getJSONObject("main")
                    temperatures[i] = main.getDouble("temp").toInt()

                    val clouds = record.getJSONObject("clouds")
                    clouds_v[i] = clouds.getDouble("all").toInt()

                    val weather = record.getJSONArray("weather").getJSONObject(0)
                    icons[i] = weather.getString("icon")

                    val forecastT: Long = record.getLong("dt") * 1000
                    data_axis[i] =
                        SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH).format(Date(forecastT))
                    x[i] = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(forecastT))
                }

               // e.g. 03/05 20:00 image 13°C
                for (i in 0 until cnt) {
                    forecastText += String.format("%-30s %d°C%n%n", data_axis[i], temperatures[i])
                }

                binding.graphTxt.text = forecastText

                val images = arrayOf(binding.image0, binding.image1, binding.image2,
                    binding.image3, binding.image4, binding.image5, binding.image6,
                    binding.image7, binding.image8)

                for (i in 0 until cnt) {
                    Picasso.get().load("http://openweathermap.org/img/wn/" + icons[i] + "@2x.png")
                        .resize(200, 200).into(images[i])
                }


            } catch (e: Exception) {
                val error = e.toString()
                binding.graphTxt.text = error
            }
        }
    }
}

