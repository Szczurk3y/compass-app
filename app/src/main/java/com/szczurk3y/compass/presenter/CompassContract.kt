package com.szczurk3y.compass.presenter

import android.app.Activity
import android.view.animation.RotateAnimation
import androidx.fragment.app.FragmentManager

interface CompassContract {
    interface Presenter {
        fun rotateNeedle(fromAzimuth: Float, toAzimuth: Float)
        fun rotateArrow(fromAzimuth: Float, toAzimuth: Float)
        fun updateDestination(fragmentManager: FragmentManager)
        fun requestPermission()
        fun locationChanged(latitude: String, longitude: String)
        fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    }


    interface View {
        fun showNeedleRotated(anim: RotateAnimation)
        fun showArrowRotated(anim: RotateAnimation)
        fun showLocationError(textError1: String, textError2: String)
        fun runGPS()
        fun showUpdatedDestination(latitude: String, longitude: String)
        fun showUpdatedDestinationError()
        fun getViewActivity(): Activity
    }
}