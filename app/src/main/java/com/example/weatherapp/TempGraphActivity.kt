package com.example.weatherapp


import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class TempGraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_graph)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val data = ArrayList<Entry>()

        for (i in 0 until cnt) {
            if (temp_graph) {
                data.add(Entry(0f + i, temperatures[i].toFloat()))
            } else if (pop_graph) {
                data.add(Entry(0f + i * 1f, pop[i].toFloat()))
            } else {
                data.add(Entry(0f + i * 1f, clouds_v[i].toFloat()))
            }
        }

        val txt = findViewById<TextView>(R.id.graphTxt)
        when {
            temp_graph -> {
                txt.text = getString(R.string.temperatureGraphTitle)
            }
            pop_graph -> {
                txt.text = getString(R.string.precipitationGraphTitle)
            }
            else -> {
                txt.text = getString(R.string.cloudinessGraphTitle)
            }
        }

        val dataset = LineDataSet(data, "")

        dataset.setDrawValues(true)
        dataset.setDrawFilled(true)
        dataset.lineWidth = 5f
        dataset.valueTextColor = Color.rgb(255, 255, 83)
        dataset.valueTextSize = 20F

        val lineChart = findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)

        dataset.fillColor = ContextCompat.getColor(this, R.color.dark_orange)
        dataset.fillAlpha = 100
        dataset.color = ContextCompat.getColor(this, R.color.dark_orange)
        dataset.valueTextSize = 16f
        // dataset.setValueTextColors(mutableListOf(255, 255, 255))
        lineChart.data = LineData(dataset)
        lineChart.axisRight.isEnabled = false

        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.animateX(1800, Easing.EaseInExpo)
        lineChart.xAxis.setAvoidFirstLastClipping(false)
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(x)
        lineChart.xAxis.textColor = ContextCompat.getColor(this, R.color.dark_orange)
        lineChart.xAxis.textSize = 20F

        lineChart.getAxis(YAxis.AxisDependency.LEFT).textColor =
            ContextCompat.getColor(this, R.color.dark_orange)
        lineChart.getAxis(YAxis.AxisDependency.LEFT).textSize = 20F
        lineChart.getAxis(YAxis.AxisDependency.LEFT).axisMinimum = 0F
        lineChart.xAxis.granularity = 1.0f

        if (temp_graph) {
            val m: Int = temperatures.maxOrNull() ?: 0
            lineChart.getAxis(YAxis.AxisDependency.LEFT).axisMaximum = (m * 1.2).toFloat()
        }

        lineChart.setExtraOffsets(20F, 50F, 50F, 50F)

        lineChart.legend.isEnabled = false

        val description = Description()
        description.text = ""
        lineChart.description = description
    }

}

