package com.example.weatherapp


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityForecastBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

val size = 10
var clouds_v = Array(size) {0}
var pop = Array(size) {0}
var temperatures = Array(size) {0}
var data_axis = Array(size) {""}
var x = Array(size) {""}
var icons = Array(size) {""}

class ForecastActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForecastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        readForecast().execute()

        //TEMPERATURE GRAPH
        binding.tempGraphBtn.setOnClickListener {
            temp_graph = true
            pop_graph = false
            clouds_graph = false
            startActivity(Intent(this, TempGraphActivity::class.java))
        }
        //POP GRAPH
        binding.popGraphBtn.setOnClickListener {
            temp_graph = false
            pop_graph = true
            clouds_graph = false
            startActivity(Intent(this, TempGraphActivity::class.java))
        }
        //CLOUDS GRAPH
        binding.cloudsGraphBtn.setOnClickListener {
            temp_graph = false
            pop_graph = false
            clouds_graph = true
            startActivity(Intent(this, TempGraphActivity::class.java))
        }
    }

        inner class readForecast() : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg params: String?): String? {
                var history_data:String?
                history_data = try {
                    URL("https://api.openweathermap.org/data/2.5/forecast?q=$location&units=metric&cnt=$cnt&appid=$key").readText(Charsets.UTF_8)
                } catch (e: Exception) {
                    null
                }
                return history_data
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                try {
                    val jsonObj = JSONObject(result)
                    var forecast_text = ""

                    for (i in 0 until cnt) {
                        val record = jsonObj.getJSONArray("list").getJSONObject(i)
                        pop[i] = (100 * record.getDouble("pop")).toInt()

                        val main = record.getJSONObject("main")
                        temperatures[i] = main.getDouble("temp").toInt()

                        val clouds = record.getJSONObject("clouds")
                        clouds_v[i] = clouds.getDouble("all").toInt()

                        val weather = record.getJSONArray("weather").getJSONObject(0)
                        icons[i] = weather.getString("icon")

                        val forecast_t : Long = record.getLong("dt") * 1000
                        data_axis[i] = SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH).format(Date(forecast_t))
                        x[i] = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(forecast_t))
                    }

                    for (i in 0 until cnt) {
                        forecast_text += data_axis[i] + "                    " + temperatures[i].toString() + "°C" + System.lineSeparator() + System.lineSeparator() }

                    binding.graphTxt.text = forecast_text

                    val images = arrayOf(binding.image0, binding.image1, binding.image2,
                        binding.image3, binding.image4, binding.image5,binding.image6,
                        binding.image7,binding.image8)

                    for(i in 0 until cnt)
                    {
                        Picasso.get().load("http://openweathermap.org/img/wn/" + icons[i] +"@2x.png").resize(200,200).into(images[i])
                    }


                }
                catch (e: Exception)
                {
                    val error = e.toString()
                    binding.graphTxt.text = error

                }

            }
        }
    }

