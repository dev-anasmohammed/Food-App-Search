package com.devanasmohammed.search.util

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.devanasmohammed.search.R

/**
 * This class used to display Custom Dialog Box
 */
class AlertDialogHandler {

    /**
     * This method used to show dialog
     *
     * @param activity activity to show dialog on it
     * @param title the title of the dialog
     * @param msg the msg of the dialog
     * @param isCancelable if the dialog is cancelable
     * @param function if you want to run function parse it ex: like this ::loginUsingFirebase
     * for the method called loginUsingFirebase()
     */
    fun showDialog(
        activity: Activity,
        title: String,
        msg: String,
        isCancelable: Boolean,
        function: (() -> Unit)?
    ) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCancelable)
        dialog.setContentView(R.layout.alert_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //animation zoom in and out
        val window = dialog.window!!
        window.setGravity(Gravity.CENTER)
        window.attributes.windowAnimations = R.style.Widget_Drivien_DialogAnimation

        //find views
        val titleView: TextView = dialog.findViewById(R.id.alert_dialog_title)
        val msgView: TextView = dialog.findViewById(R.id.alert_dialog_msg)
        val okButton: Button = dialog.findViewById(R.id.alert_dialog_ok_button)
        val cancelButton: Button = dialog.findViewById(R.id.alert_dialog_cacenl_button)

        //if it setCancelable -> false hide cancel button
        if(isCancelable){
            cancelButton.visibility = View.VISIBLE
        }else{
            cancelButton.visibility = View.INVISIBLE
        }

        //set title and msg to views
        titleView.text = title
        msgView.text = msg

        //handle buttons
        //if function parameter is null just close the the dialog
        //if function not null dismiss dialog then call function
        okButton.setOnClickListener {
            dialog.dismiss()
            if (function != null) function()
        }

        //just dismiss the dialog
        cancelButton.setOnClickListener {
            if(isCancelable){
                dialog.dismiss()
            }
        }

        window.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

}