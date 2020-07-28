package com.szczurk3y.compass.view.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import com.szczurk3y.compass.model.CompassStore

class Compass (
    private val listener: SensorListener,
    sensorManager: SensorManager
) : SensorEventListener {

    private val gravity: FloatArray = FloatArray(3)
    private val geomagnetic: FloatArray = FloatArray(3)
    private val alpha = 0.97f
    private var currentNeedleAzimuth = 0f
    private var lastNeedleAzimuth = 0f

    init {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        TODO("Not yet implemented")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when(event!!.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                gravity[0] = alpha*gravity[0]+(1-alpha)*event.values[0]
                gravity[1] = alpha*gravity[1]+(1-alpha)*event.values[1]
                gravity[2] = alpha*gravity[2]+(1-alpha)*event.values[2]
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                geomagnetic[0] = alpha*geomagnetic[0]+(1-alpha)*event.values[0]
                geomagnetic[1] = alpha*geomagnetic[1]+(1-alpha)*event.values[1]
                geomagnetic[2] = alpha*geomagnetic[2]+(1-alpha)*event.values[2]
            }
        }

        updateNeedle()
    }

    private fun updateNeedle() {
        val R = FloatArray(9)
        val I = FloatArray(9)

        val success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(R, orientation)
            currentNeedleAzimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
            currentNeedleAzimuth = (currentNeedleAzimuth+360)%360
            listener.onCompassSensorChanged(CompassStore.ROTATE_NEEDLE, currentNeedleAzimuth, lastNeedleAzimuth)
            lastNeedleAzimuth = currentNeedleAzimuth
        }
    }

    interface SensorListener {
        fun onCompassSensorChanged(updateType: Int, fromAzimuth: Float, toAzimuth: Float)
    }
}