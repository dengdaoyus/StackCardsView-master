package com.beyondsw.widget.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.beyondsw.widget.R
import kotlinx.android.synthetic.main.avtivity_ripplespread.*
import android.graphics.Color.parseColor
import android.graphics.Paint
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import com.beyondsw.lib.widget.DisplayUtils


/**
 * 仿支付宝咻一咻
 * Created by Administrator on 2018/3/22 0022.
 */

open class RippleSpreadTestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avtivity_ripplespread)
        rippleSpreadView.setInitialRadius(DisplayUtils.dip2px(this,60f).toFloat())
        rippleSpreadView.setStyle(Paint.Style.FILL)
        rippleSpreadView.setColor(Color.parseColor("#DBDBFF"))
//        rippleSpreadView.setInterpolator(LinearOutSlowInInterpolator())
        rippleSpreadView.start()
        initView()
    }

    private fun initView() {
        button.setOnClickListener {
            rippleSpreadView.stop()
        }
    }

}
