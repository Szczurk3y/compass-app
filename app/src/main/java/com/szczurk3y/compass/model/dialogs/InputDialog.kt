package com.szczurk3y.compass.model.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.szczurk3y.compass.R
import kotlinx.android.synthetic.main.dialog_input.view.*

class InputDialog(
    private val callback: ResultListener,
    private val title: Int
) : DialogFragment() {
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var buttonConfirm: Button
    private lateinit var buttonCancel: Button
    private lateinit var header: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_input, null)

        init(view)
        configureListeners()

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(view)
            builder.create()
        } ?: throw IllegalAccessException("Activity cannot be null")
    }

    private fun init(view: View) {
        latitude = view.enterLatitude
        longitude = view.enterLongitude
        buttonConfirm = view.confirm_buttom
        buttonCancel = view.cancel_buttom
        view.header.text = getString(title)
    }

    private fun configureListeners() {
        buttonConfirm.setOnClickListener {
            if (latitude.text.toString().isNotEmpty() && longitude.text.toString().isNotEmpty()) {
                callback.onInputDialogResult(
                    latitude = latitude.text.toString(),
                    longitude = longitude.text.toString()
                )
            } else {
                callback.onInputDialogResultError()
            }
            dialog?.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog?.cancel()
        }
    }

    interface ResultListener {
        fun onInputDialogResult(latitude: String, longitude: String)
        fun onInputDialogResultError()
    }
}