package com.example.magneticorientation

import androidx.lifecycle.LiveData
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

// 向き情報を格納するクラス
data class Orientation(
    val azimuth: Float,
    val pitch: Float,
    val roll: Float
)

class magneticOrientation(
    context: Context,
    private val sensorDelay: Int = SensorManager.SENSOR_DELAY_UI)
    : LiveData<Orientation>(), SensorEventListener {

    //センサーマネージャーを取得
    private val mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    //加速度と地磁気センサーを利用
    private val accelerometer: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val magneticField: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private val mAccelerometerReading = FloatArray(3)
    private val mMagnetometerReading = FloatArray(3)

    private val mRotationMatrix = FloatArray(9)
    private val mOrientationAngles = FloatArray(3)

    override fun onActive() {
        super.onActive()

        //https://www.support.softbankmobile.co.jp/partner_st/home_tech9/column7-3.cfm
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, sensorDelay)
        mSensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, sensorDelay)
    }

    override fun onInactive() {
        super.onInactive()
        mSensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    //センサーデータを取得したとき
    override fun onSensorChanged(event: SensorEvent) {

        if (event.sensor == accelerometer) {
            //配列のコピー　コピー元配列、コピー元配列の開始位置、コピー先配列、コピー先配列の開始位置、コピーサイズ
            //センサーデータをmAccelerometerReadingにコピー
            System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.size)
        } else if (event.sensor == magneticField) {
            System.arraycopy(event.values, 0, mMagnetometerReading, 0, mMagnetometerReading.size)
        }

        updateOrientationAngles()

        value = Orientation(mOrientationAngles[0], mOrientationAngles[1], mOrientationAngles[2])
    }

    private fun updateOrientationAngles() {
        //回転行列を計算
        //
        SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading)
        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles)
    }
}