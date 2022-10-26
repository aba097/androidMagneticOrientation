package com.example.magneticorientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    //Activityが生成されたときに呼び出される
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        magneticOrientation(this).observe(this, Observer { orientation ->

            if (orientation == null) return@Observer
            Log.e("orientation", Math.toDegrees(orientation.pitch.toDouble()).toString() + ", " + Math.toDegrees(orientation.roll.toDouble()).toString() + ", " + Math.toDegrees(orientation.azimuth.toDouble()).toString())
            // orientation.azimuth
            // orientation.pitch
            // orientation.roll
            // で処理を行う
        })
    }

}