package com.szczurk3y.compass.presenter

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.szczurk3y.compass.R
import com.szczurk3y.compass.app.CompassApplication
import com.szczurk3y.compass.model.CompassStore
import com.szczurk3y.compass.model.dialogs.InputDialog

class CompassPresenter: BasePresenter<CompassContract.View>(), InputDialog.ResultListener, CompassContract.Presenter {
    override fun rotateNeedle(fromAzimuth: Float, toAzimuth: Float) {
        val anim = RotateAnimation(-fromAzimuth, -toAzimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 500
        anim.repeatCount = 0
        anim.fillAfter = true
        getView()?.showNeedleRotated(anim)
    }

    override fun rotateArrow(fromAzimuth: Float, toAzimuth: Float) {
        val anim = RotateAnimation(-fromAzimuth, -toAzimuth, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.duration = 500
        anim.repeatCount = 0
        anim.fillAfter = true
        getView()?.showArrowRotated(anim)
    }

    override fun updateDestination(fragmentManager: FragmentManager) {
        InputDialog(this, R.string.confirm_button, R.string.cancel_button).show(fragmentManager, "Enter location")
    }

    override fun locationChanged(latitude: String, longitude: String) {
        CompassStore.currentLatitude = latitude.toFloat()
        CompassStore.currentLongitude = longitude.toFloat()
    }

    override fun requestPermission() {
        val permissionFineLocation = ContextCompat.checkSelfPermission(
            getView()?.getViewActivity()!!, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionCoarseLocation = ContextCompat.checkSelfPermission(
            getView()?.getViewActivity()!!, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED || permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                getView()?.getViewActivity()!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                CompassStore.REQUEST_RECORD_CODE
            )
        } else {
            getView()?.runGPS()
        }
    }

    override fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            CompassStore.REQUEST_RECORD_CODE -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    getView()?.showLocationError(
                        getView()?.getViewActivity()?.getString(R.string.permissionsLatitudeNeeded)!!,
                        getView()?.getViewActivity()?.getString(R.string.permissionsLongitudeNeeded)!!
                    )
                } else {
                    getView()?.runGPS()
                }
            }
        }
    }

    override fun onInputDialogResult(latitude: String, longitude: String) {
        val latitudeTransformed = latitude.replace(",", ".")
        val longitudeTransformed = longitude.replace(",", ".")
        CompassStore.directionLatitude = latitudeTransformed.toFloat()
        CompassStore.directionLongitude = longitudeTransformed.toFloat()

        getView()?.showUpdatedDestination(latitudeTransformed, longitudeTransformed)
    }


    override fun onInputDialogResultError() {
        getView()?.showUpdatedDestinationError()
    }
}