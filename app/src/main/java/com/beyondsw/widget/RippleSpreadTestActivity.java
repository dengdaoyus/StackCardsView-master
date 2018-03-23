package com.beyondsw.widget;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * 仿支付宝咻一咻
 * Created by Administrator on 2018/3/22 0022.
 */

public class RippleSpreadTestActivity extends AppCompatActivity {

    Button button;
    WhewView whenView;
    RippleSpreadView rippleSpreadView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_ripplespread);
        initView();

    }


    protected void initView() {
        rippleSpreadView = (RippleSpreadView) findViewById(R.id.rippleSpreadView);
        whenView = (WhewView) findViewById(R.id.whenView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!whenView.isStarting()) {
                    whenView.start();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(RippleSpreadTestActivity.this, DemoActivity.class));
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (whenView.isStarting()) {
            whenView.stop();
        }
    }
}
