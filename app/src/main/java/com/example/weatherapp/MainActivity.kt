package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


var location: String = "poznan,pl"
val key: String = "803f090413c6625d82e7002e2713951f"
val cnt = 9

var loc = ""
var update_t : Long = 0
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

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        readTask().execute()
        progressBar.max = 50


        //12H OR 24H FORMAT
        binding.btnTimeFormat.setOnClickListener{
            if(english)
            {
                english = false
                btnTimeFormat.text = "12h"
            }
            else
            {
                english = true
                btnTimeFormat.text = "24h"
            }
            readTask().execute()
        }
        //TEMPERATURE MORE INFO
        binding.btnTemperature.setOnClickListener{ startActivity(Intent(this, TemperatureActivity::class.java)) }

        //FORECAST
        binding.btnForecast.setOnClickListener{ startActivity(Intent(this, ForecastActivity::class.java)) }

        //HISTORY ACTIVITY
        binding.btnHistory.setOnClickListener{ startActivity(Intent(this, HistoryActivity::class.java)) }

        //CITY INPUT
        binding.btnSearch.setOnClickListener{
            location = binding.inputCity.text.toString()
            readTask().execute()
        }

    }

    inner class readTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var data:String?
            try
            {
                data = URL("https://api.openweathermap.org/data/2.5/weather?q=$location&units=metric&lang=pl&appid=$key").readText(Charsets.UTF_8)
            }
            catch (e: Exception)
            {
                data = null
            }
            return data
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")

                //LOCATION
                loc = jsonObj.getString("name")+", "+sys.getString("country")
                binding.txtLocation.text = loc

                //LAST UPDATE
                update_t = jsonObj.getLong("dt")
                update_t *= 1000

                var update_text:String?
                if(english)
                {
                    update_text = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(update_t))
                }
                else
                {
                    update_text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(Date(update_t))
                }

                binding.txtLastUpdate.text =  update_text

                //TEMPERATURE
                temp =  main.getDouble("temp").toInt()
                progressBar.progress = temp

                binding.temperature.text = temp.toString() + "??C"

                //PRESSURE, WIND
                humidity = main.getString("humidity")
                pressure_v = main.getString("pressure")

                v_wind = wind.getString("speed")
                binding.wind.text = v_wind + " m/s"
                binding.pressure.text = pressure_v + " hPa"

                //SUNRISE, SUNSET
                var sunrise:Long = sys.getLong("sunrise")
                sunrise *= 1000
                var sunset:Long = sys.getLong("sunset")
                sunset *= 1000

                val sunrise_text :String?
                val sunset_text :String?
                if(english)
                {
                    sunrise_text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise))
                    sunset_text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset))
                }
                else
                {
                    sunrise_text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunrise))
                    sunset_text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(sunset))
                }
                binding.sunrise.text = sunrise_text
                binding.sunset.text = sunset_text

                //TEMPERATURE - MORE INFO
                feels_like =  main.getString("feels_like")
                temp_max = main.getString("temp_max")
                temp_min = main.getString("temp_min")

                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val main_icon = weather.getString("icon")
                Picasso.get().load("http://openweathermap.org/img/wn/" + main_icon +"@2x.png").resize(400,400).into(binding.infoView3)


            }
            catch (e: Exception)
            {
                val wifi = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                if (!wifi.isWifiEnabled) {
                    Toast.makeText(this@MainActivity, getString(R.string.internet_error), Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@MainActivity, getString(R.string.input_error), Toast.LENGTH_SHORT).show()
                }


            }

        }
    }

    //SAVE BUTTON
    fun save(v: View?)
    {
        //DATA TO FILE
        val filename = "logs.csv"
        val fos = openFileOutput(filename, MODE_APPEND)
        val v1 = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(Date(update_t)) + ","

        try
        {
            fos.write(("$v1 $loc").toByteArray())
            //NEW LINE
            fos.write(System.getProperty("line.separator").toByteArray());

            fos.write((temp.toString() + "??C, " + humidity + "%, " + pressure_v + "hPa").toByteArray())
            fos.write(System.getProperty("line.separator").toByteArray());
            fos.write(System.getProperty("line.separator").toByteArray());

            fos.close()

            Toast.makeText(this@MainActivity, getString(R.string.toast_save_text), Toast.LENGTH_SHORT).show()

        }
        catch(e: Exception)
        {
            Toast.makeText(this@MainActivity, getString(R.string.save_error), Toast.LENGTH_SHORT).show()
        }
    }


}