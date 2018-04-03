package com.beyondsw.widget.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.beyondsw.widget.R
import kotlinx.android.synthetic.main.avtivity_ripplespread.*


/**
 * 仿支付宝咻一咻
 * Created by Administrator on 2018/3/22 0022.
 */

open class RippleSpreadTestActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avtivity_ripplespread)
        initView()
    }

    private fun initView() {
        button.setOnClickListener {
            if (rippleSpreadView.isAdmin) {
                rippleSpreadView.stopAdmin()
            } else {
                rippleSpreadView.startAdmin()
            }
        }
    }

}
