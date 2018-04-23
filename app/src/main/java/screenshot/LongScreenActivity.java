package screenshot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.beyondsw.widget.R;

import java.util.ArrayList;
import java.util.List;

import screenshot.adapter.ImageListAdapter;
import screenshot.service.FloatWindowsService;
import screenshot.utils.FileUtils;


public class LongScreenActivity extends AppCompatActivity {

    private ListView img_list;
    private ImageListAdapter mAdapter;
    private List<String> filePathList = new ArrayList<>();

    public final static int REQUEST_CODE_STORAGE = 200;
    /***
     * 请求悬浮窗权限
     * */
    public static final int REQUEST_WINDOW_GRANT = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longscreen);

        img_list = (ListView) findViewById(R.id.img_list);
        mAdapter = new ImageListAdapter(filePathList);
        img_list.setAdapter(mAdapter);

        int result = ContextCompat.checkSelfPermission(LongScreenActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LongScreenActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
        } else {
            refreshData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.length > 0) {
                refreshData();
            }
        }
    }

    public void refreshData() {
        filePathList.clear();
        filePathList.addAll(FileUtils.getFileList());
        mAdapter.notifyDataSetChanged();
    }


    public static final int REQUEST_MEDIA_PROJECTION = 18;
    MediaProjectionManager mediaProjectionManager;

    public void start(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 动态申请悬浮窗权限
            if (!Settings.canDrawOverlays(LongScreenActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_WINDOW_GRANT);
            }
            else {
                initMediaProjectionManager();
            }
        } else {
            initMediaProjectionManager();
        }


    }

    private void initMediaProjectionManager() {
        if (mediaProjectionManager != null) {
            return;
        }
        // How to use MediaProjectionManager
        // 1.get an instance of MediaProjectionManager
        mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        // 2.create the permissions intent and show it to user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(
                    mediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        }
    }

    // 3.handle the onActivityResult callback to get a MediaProjection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_MEDIA_PROJECTION:
                if (resultCode == RESULT_OK && data != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mediaProjection = this.mediaProjectionManager.getMediaProjection(resultCode, data);
                    }
                    startService(new Intent(LongScreenActivity.this, FloatWindowsService.class));
                    moveTaskToBack(false);
                }
                break;
            case REQUEST_WINDOW_GRANT:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(LongScreenActivity.this)) {
                        Toast.makeText(LongScreenActivity.this, "没有打开悬浮权限~，", Toast.LENGTH_SHORT).show();
                    } else {
                       initMediaProjectionManager();
                    }
                }
                break;
        }
    }

    private static MediaProjection mediaProjection = null;

    public static MediaProjection getMediaProjection() {
        return mediaProjection;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, FloatWindowsService.class));
        if (mediaProjection != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaProjection.stop();
            }
            mediaProjection = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

}
