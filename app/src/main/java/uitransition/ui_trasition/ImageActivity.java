package uitransition.ui_trasition;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beyondsw.widget.R;

import uitransition.CustomImage;


/**
 * Created by Qiushui on 2016/10/8.
 */

public class ImageActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void imageClick(View view) {
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(this, view, getString(R.string.image_transition_name));

        CustomImage image = (CustomImage) view;
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_IMAGE, image.getImageId());
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        setSupportActionBar((Toolbar) findViewById(R.id.activity_img_toolbar));
    }
}
