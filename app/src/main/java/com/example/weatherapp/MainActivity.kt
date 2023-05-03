package com.example.weatherapp

import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


var location = "poznan,pl"
const val key: String = "803f090413c6625d82e7002e2713951f"
const val cnt = 9

var loc = ""
var update_t: Long = 0
var temp = 0
var humidity = ""
var pressure_v = ""
var v_wind = ""
var english = false
var feels_like = ""
var temp_min = ""
var temp_max = ""

var temp_graph = false
var pop_graph = false
var clouds_graph = false


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        ReadTask().execute()

        //12H OR 24H FORMAT
        binding.btnTimeFormat.setOnClickListener {
            if (english)
                btnTimeFormat.text = getString(R.string.text12h)
            else
                btnTimeFormat.text = getString(R.string.text24h)

            english = !english
            ReadTask().execute()
        }
        //TEMPERATURE MORE INFO
        binding.btnTemperature.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    TemperatureActivity::class.java
                )
            )
        }

        //FORECAST
        binding.btnForecast.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ForecastActivity::class.java
                )
            )
        }

        //HISTORY ACTIVITY
        binding.btnHistory.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    HistoryActivity::class.java
                )
            )
        }

        //CITY INPUT
        binding.btnSearch.setOnClickListener {
            location = binding.inputCity.text.toString()
            ReadTask().execute()
        }

        //SAVE BUTTON
        binding.btnSave.setOnClickListener {
            //DATA TO FILE
            val filename = "logs.csv"
            val file = File(applicationContext.filesDir, filename)

            val v1 = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(Date(update_t)) + ","
            val data = "$v1 $loc\n$temp°C, $humidity%, $pressure_v hPa\n\n"

            try {
                val fileWriter = FileWriter(file, true)
                val bufferedWriter = BufferedWriter(fileWriter)
                bufferedWriter.write(data)
                bufferedWriter.close()
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.toast_save_text),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity,
                    getString(R.string.save_error),
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    inner class ReadTask : CoroutineScope {

        private val job = Job()

        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.IO

        fun execute() = launch {
            val result = readData()
            withContext(Dispatchers.Main) {
                if (result != "") //if URL return correct data
                    handleData(result)
            }
        }

        private suspend fun readData(): String {
            var data = ""
            try {
                data =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$location&units=metric&lang=pl&appid=$key").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                // change to main UI thread
                withContext(Dispatchers.Main) {
                    val wifi = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                    if (!wifi.isWifiEnabled) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.internet_error),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.input_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            return data
        }

        private suspend fun handleData(result: String) {
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")

                //LOCATION
                loc = jsonObj.getString("name") + ", " + sys.getString("country")
                binding.txtLocation.text = loc

                //LAST UPDATE
                update_t = jsonObj.getLong("dt") * 1000

                val updateText: String? = if (english) {
                    SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(update_t))
                } else {
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(Date(update_t))
                }
                binding.txtLastUpdate.text = updateText

                //TEMPERATURE
                temp = main.getDouble("temp").toInt()
                binding.temperature.text = getString(R.string.tempValue, temp.toString())

                //PRESSURE, WIND
                humidity = main.getString("humidity")
                pressure_v = main.getString("pressure")

                v_wind = wind.getString("speed")
                binding.wind.text = getString(R.string.wind, v_wind)
                binding.pressure.text = getString(R.string.pressure, pressure_v)

                //SUNRISE, SUNSET
                val sunrise: Long = sys.getLong("sunrise") * 1000
                val sunset: Long = sys.getLong("sunset") * 1000

                val dateFormat = if (english) {
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                } else {
                    SimpleDateFormat("HH:mm", Locale.ENGLISH)
                }
                binding.sunrise.text = dateFormat.format(Date(sunrise))
                binding.sunset.text = dateFormat.format(Date(sunset))

                //TEMPERATURE - MORE INFO
                feels_like = main.getString("feels_like")
                temp_max = main.getString("temp_max")
                temp_min = main.getString("temp_min")

                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val mainIcon = weather.getString("icon")
                Picasso.get().load("http://openweathermap.org/img/wn/$mainIcon@2x.png")
                    .resize(400, 400).into(binding.infoView3)

            } catch (e: Exception) {
                // change to main UI thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.JSON_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}