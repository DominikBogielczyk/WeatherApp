package com.example.weatherapp


import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class GraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
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

        val lineChart: LineChart by lazy { findViewById(R.id.lineChart) }
        val dataset = LineDataSet(data, "")

        dataset.apply {
            setDrawValues(true)
            setDrawFilled(true)
            lineWidth = 5f
            valueTextColor = Color.rgb(255, 255, 83)
            valueTextSize = 20F
            fillColor = ContextCompat.getColor(this@GraphActivity, R.color.dark_orange)
            fillAlpha = 100
            color = ContextCompat.getColor(this@GraphActivity, R.color.dark_orange)
            valueTextSize = 16f
        }

        lineChart.data = LineData(dataset)
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(x)
        lineChart.apply {
            axisRight.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            animateX(1800, Easing.EaseInExpo)
            xAxis.apply {
                setAvoidFirstLastClipping(false)
                textColor = ContextCompat.getColor(this@GraphActivity, R.color.dark_orange)
                textSize = 20F
                granularity = 1.0f
            }
            getAxis(YAxis.AxisDependency.LEFT).apply {
                textColor = ContextCompat.getColor(this@GraphActivity, R.color.dark_orange)
                textSize = 20F
               // axisMinimum = 0F
                axisMaximum = 100F
            }
            setExtraOffsets(20F, 50F, 50F, 50F)
            legend.isEnabled = false
            description = Description().apply { text = "" }
        }

        if (temp_graph) {
            val m: Int = temperatures.maxOrNull() ?: 0
            lineChart.getAxis(YAxis.AxisDependency.LEFT).axisMaximum = (m * 1.2).toFloat()
        }
    }

}

