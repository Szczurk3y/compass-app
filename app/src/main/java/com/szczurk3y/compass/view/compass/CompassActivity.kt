package com.szczurk3y.compass.view.compass

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.SensorManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.szczurk3y.compass.R
import com.szczurk3y.compass.model.CompassStore
import com.szczurk3y.compass.presenter.CompassContract
import com.szczurk3y.compass.presenter.CompassPresenter
import com.szczurk3y.compass.view.location.Gps
import kotlinx.android.synthetic.main.activity_compass.*

class CompassActivity : AppCompatActivity(), Compass.SensorListener, Gps.LocationListener, CompassContract.View {

    private val presenter = CompassPresenter()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        presenter.setView(this)

        configureClickListeners()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Compass(this, sensorManager)

        presenter.requestPermission()
    }

    private fun configureClickListeners() {
        latitudeButton.setOnClickListener {
            presenter.updateDestination(supportFragmentManager)
        }

        longitudeButton.setOnClickListener {
            presenter.updateDestination(supportFragmentManager)
        }

        current_latitude.setOnClickListener {
            presenter.requestPermission()
        }

        current_longitude.setOnClickListener {
            presenter.requestPermission()
        }
    }

    override fun showNeedleRotated(anim: RotateAnimation) {
        this.compass_needle.startAnimation(anim)
    }

    override fun showArrowRotated(anim: RotateAnimation) {
//        TODO("Not yet implemented")
    }

    override fun showLocationError(textError1: String, textError2: String) {
        this.current_latitude.text = textError1
        this.current_longitude.text = textError2
    }

    @SuppressLint("MissingPermission")
    override fun runGPS() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Gps(this, locationManager)
    }

    override fun showUpdatedDestination(latitude: String, longitude: String) {
        this.direction_latitude.text = latitude
        this.direction_longitude.text = longitude
    }

    override fun showUpdatedDestinationError() {
        Toast.makeText(this, getString(R.string.update_destination_error), Toast.LENGTH_SHORT).show()
    }

    override fun getViewActivity(): Activity {
        return this
    }

    override fun onCompassSensorChanged(updateType: Int, fromAzimuth: Float, toAzimuth: Float) {
        when(updateType) {
            CompassStore.ROTATE_NEEDLE ->
                presenter.rotateNeedle(fromAzimuth, toAzimuth)
            CompassStore.ROTATE_ARROW ->
                presenter.rotateArrow(fromAzimuth, toAzimuth)
        }
    }

    override fun onGpsLocationChanged(latitude: String, longitude: String) {
        this.current_latitude.text = latitude
        this.current_longitude.text = longitude
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onPermissionResult(requestCode, permissions, grantResults)
    }
}
