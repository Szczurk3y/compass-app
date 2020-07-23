package com.szczurk3y.compass.app

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.szczurk3y.compass.presenter.CompassContract
import com.szczurk3y.compass.presenter.CompassPresenter
import com.szczurk3y.compass.view.compass.Compass

class CompassApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}