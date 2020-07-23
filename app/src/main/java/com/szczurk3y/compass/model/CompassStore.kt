package com.szczurk3y.compass.model

object CompassStore {
    const val ROTATE_NEEDLE = 1
    const val ROTATE_ARROW = 2
    const val REQUEST_RECORD_CODE = 1
    var currentLatitude: Float = 0f
    var currentLongitude: Float = 0f
    var directionLatitude: Float = 0f
    var directionLongitude: Float = 0f
}