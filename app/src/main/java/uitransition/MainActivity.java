package uitransition;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


import com.beyondsw.widget.R;

import uitransition.manual_transition.AnimeActivity;
import uitransition.ui_trasition.ImageActivity;


public class MainActivity extends AppCompatActivity {

    private Button mAutoTransitionBtn;
    private Button mManualTransitionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mAutoTransitionBtn = (Button) findViewById(R.id.ui_transition_btn);
        mManualTransitionBtn = (Button) findViewById(R.id.manual_transition_btn);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            mAutoTransitionBtn.setVisibility(View.GONE);
        }
        mAutoTransitionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ImageActivity.class));
            }
        });

        mManualTransitionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnimeActivity.class));
            }
        });
    }
}
