package com.example.weatherapp


import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter


class TempGraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_graph)

        val data = ArrayList<Entry>()

        for (i in 0 until cnt)
        {
            if(temp_graph)
            {
                data.add(Entry(0f + i*1f, temperatures[i].toFloat()))
            }
            else if(pop_graph)
            {
                data.add(Entry(0f + i*1f, pop[i].toFloat()))
            }
            else
            {
                data.add(Entry(0f + i*1f, clouds_v[i].toFloat()))
            }
        }

        val txt = findViewById<TextView>(R.id.graph_txt)
        when {
            temp_graph -> {
                txt.text = "Temperature graph, °C"}
            pop_graph -> { txt.text = "Probability of precipitation, %" }
            else -> { txt.text = "Cloudiness, %" }
        }

        val dataset = LineDataSet(data, "")

        dataset.setDrawValues(true)
        dataset.setDrawFilled(true)
        dataset.lineWidth = 5f
        dataset.setValueTextColor(Color.rgb(255, 255, 83))
        dataset.setValueTextSize(20F)

        val lineChart = findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)

        dataset.fillColor = resources.getColor(R.color.dark_orange)
        dataset.fillAlpha = 100
        dataset.color = resources.getColor(R.color.dark_orange)
        dataset.setValueTextSize(16f)
        // dataset.setValueTextColors(mutableListOf(255, 255, 255))
        lineChart.data = LineData(dataset)
        lineChart.axisRight.isEnabled = false

        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.animateX(1800, Easing.EaseInExpo)
        lineChart.xAxis.setAvoidFirstLastClipping(false)
        lineChart.xAxis.setValueFormatter(IndexAxisValueFormatter(x))
        lineChart.xAxis.textColor= resources.getColor(R.color.dark_orange)
        lineChart.xAxis.textSize = 20F
        lineChart.getAxis(YAxis.AxisDependency.LEFT).textColor= resources.getColor(R.color.dark_orange)
        lineChart.getAxis(YAxis.AxisDependency.LEFT).textSize = 20F
        lineChart.getAxis(YAxis.AxisDependency.LEFT).axisMinimum = 0F
        lineChart.xAxis.setGranularity(1.0f)

        if(temp_graph)
        {
            val m: Int = temperatures.maxOrNull() ?: 0
            lineChart.getAxis(YAxis.AxisDependency.LEFT).axisMaximum =  (m*1.2).toFloat()
        }

        lineChart.setExtraOffsets(20F, 50F, 50F, 50F)

        lineChart.legend.isEnabled = false

        val description = Description()
        description.text = ""
        lineChart.setDescription(description)
    }

}

