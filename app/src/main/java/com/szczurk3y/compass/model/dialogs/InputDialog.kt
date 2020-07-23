package com.szczurk3y.compass.model.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.szczurk3y.compass.R
import kotlinx.android.synthetic.main.dialog_input.view.*

class InputDialog(
    private val callback: ResultListener,
    private val confirmText: Int,
    private val cancelText: Int
) : DialogFragment() {
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_input, null)
        fetchViews(view)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(view)
                .setPositiveButton(getString(confirmText)) {dialog, id ->
                    if (latitude.text.toString().isNotEmpty() && longitude.text.toString().isNotEmpty()) {
                        callback.onInputDialogResult(
                            latitude = latitude.text.toString(),
                            longitude = longitude.text.toString()
                        )
                    } else {
                        callback.onInputDialogResultError()
                    }
                    getDialog()?.dismiss()
                }
                .setNegativeButton(getString(cancelText)) {dialog, id ->
                    getDialog()?.cancel()
                }
            builder.create()
        } ?: throw IllegalAccessException("Activity cannot be null")
    }

    private fun fetchViews(view: View) {
        latitude = view.enterLatitude
        longitude = view.enterLongitude
    }

    interface ResultListener {
        fun onInputDialogResult(latitude: String, longitude: String)
        fun onInputDialogResultError()
    }
}