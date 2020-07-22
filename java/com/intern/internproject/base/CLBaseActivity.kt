package com.intern.internproject.base

import android.R
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet


open class CLBaseActivity : AppCompatActivity() {
    var layout: ConstraintLayout?= null
    var progressBar: ProgressBar? = null

    /**show the progress bar */
    fun showProgressBar() {
        layout = ConstraintLayout(this)
        progressBar = ProgressBar(this, null, R.attr.progressBarStyleLarge)
        progressBar?.isIndeterminate = true
        progressBar?.indeterminateDrawable?.setColorFilter(Color.parseColor("#D81B60"), PorterDuff.Mode.MULTIPLY)
        progressBar?.visibility = View.VISIBLE
        val progressBarLayoutParams = ConstraintLayout.LayoutParams(
            100,
            100
        )
        val parentLayoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        progressBarLayoutParams.bottomToBottom = ConstraintSet.PARENT_ID;
        progressBarLayoutParams.endToEnd = ConstraintSet.PARENT_ID;
        progressBarLayoutParams.startToStart = ConstraintSet.PARENT_ID;
        progressBarLayoutParams.topToTop = ConstraintSet.PARENT_ID;
        layout?.addView(progressBar, progressBarLayoutParams)
        addContentView(layout, parentLayoutParams)
    }

    /**hide the progress bar*/
    fun hideProgressBar() {
        if (layout != null) {
            layout?.visibility = View.GONE
        }
    }
}