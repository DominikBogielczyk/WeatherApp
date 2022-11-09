package com.example.weatherapp


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

var clouds_v = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
var pop = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
var temperatures = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
var data_axis = arrayOf("","","","","","","","","","")
var x = arrayOf("","","","","","","","","","")
var icons = arrayOf("","","","","","","","","","")

class ForecastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        readForecast().execute()

        //TEMPERATURE GRAPH
        val t_btn: Button = findViewById(R.id.temp_graph_btn)
        t_btn.setOnClickListener {
            temp_graph = true
            pop_graph = false
            clouds_graph = false
            startActivity(Intent(this, TempGraphActivity::class.java))
        }
        //POP GRAPH
        val p_btn: Button = findViewById(R.id.pop_graph_btn)
        p_btn.setOnClickListener {
            temp_graph = false
            pop_graph = true
            clouds_graph = false
            startActivity(Intent(this, TempGraphActivity::class.java))
        }
        //CLOUDS GRAPH
        val c_btn: Button = findViewById(R.id.clouds_graph_btn)
        c_btn.setOnClickListener {
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
                        forecast_text += data_axis[i] + "               " + temperatures[i].toString() + "°C" + System.lineSeparator() + System.lineSeparator() }

                    findViewById<TextView>(R.id.graph_txt).text = forecast_text

                    val images = arrayOf(findViewById<ImageView>(R.id.image0), findViewById<ImageView>(R.id.image1), findViewById<ImageView>(R.id.image2),
                        findViewById<ImageView>(R.id.image3), findViewById<ImageView>(R.id.image4), findViewById<ImageView>(R.id.image5),
                        findViewById<ImageView>(R.id.image6), findViewById<ImageView>(R.id.image7), findViewById<ImageView>(R.id.image8))

                    for(i in 0 until cnt)
                    {
                        Picasso.get().load("http://openweathermap.org/img/wn/" + icons[i] +"@2x.png").resize(200,200).into(images[i])
                    }


                }
                catch (e: Exception)
                {
                    val error = e.toString()
                    findViewById<TextView>(R.id.graph_txt).text = error

                }

            }
        }
    }

