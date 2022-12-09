package com.devanasmohammed.search.util

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Pulse

class ProgressBarHandler(private val activity: Activity, root: Int) {

    private val mProgressBar: ProgressBar
    private val mBackground: View
    private var isViewsCreated = false

    private val progressBarColor = "#FE724C"
    private val progressBarAlpha = 0.6f
    private val backgroundColor = "#ffffff"
    private val backgroundAlpha = 0.4f

    fun show() {
        mProgressBar.visibility = View.VISIBLE
        mBackground.visibility = View.VISIBLE

        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

    }

    fun hide() {
        mProgressBar.visibility = View.INVISIBLE
        mBackground.visibility = View.INVISIBLE

        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    init {
        //get root view
        val layout = (activity).findViewById<View>(root).rootView as ViewGroup

        //create black background
        mBackground = View(activity)
        mBackground.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        mBackground.setBackgroundColor(Color.parseColor(backgroundColor))
        mBackground.alpha = backgroundAlpha

        //create progress bar
        mProgressBar = ProgressBar(activity)

        //setup custom progress bar
        val pulse: Sprite = Pulse()
        pulse.color = Color.parseColor(progressBarColor)

        mProgressBar.indeterminateDrawable = pulse
        mProgressBar.isIndeterminate = true
        mProgressBar.layoutParams = ViewGroup.LayoutParams(100, 100)
        mProgressBar.alpha = progressBarAlpha

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        if (!isViewsCreated) {
            //add background to root
            val rl2 = RelativeLayout(activity)
            rl2.gravity = Gravity.CENTER
            rl2.addView(mBackground)
            layout.addView(rl2, params)

            //add progressbar to root
            val rl = RelativeLayout(activity)
            rl.gravity = Gravity.CENTER
            rl.addView(mProgressBar)
            layout.addView(rl, params)

            isViewsCreated = true
        }

        hide()
    }
}
