package com.beyondsw.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


/**
 * @author liye
 * @version 4.1.0
 * @since: 16/4/20 上午11:54
 */
public class DisplayUtils {
    private static final float ROUND_DIFFERENCE = 0.5f;
    private static DisplayMetrics sDisplayMetrics = null;

    public static void init(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * dp 转 px
     * 注意正负数的四舍五入规则
     *
     * @param dp dp值
     * @return 转换后的像素值
     */
    public static int dp2px(int dp) {
        return (int) (dp * sDisplayMetrics.density + (dp > 0 ? ROUND_DIFFERENCE : - ROUND_DIFFERENCE));
    }

    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位：像素
     *
     * @return 屏幕高度
     */
    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }


    //dp转化为px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //px转化为dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param
     *    （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据上下文重置DisplayMetrics
     */
    public static DisplayMetrics dueDisplayMetrics(Context context){
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics dm=dueDisplayMetrics(context);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        DisplayMetrics dm=dueDisplayMetrics(context);
        return dm.heightPixels;
    }
    /**
     * @param activity
     * @return
     */
    public static int getTitleBarHeight(Activity activity) {
        int contentTop = activity.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        return  contentTop - getStatusHeight(activity);


    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static synchronized int  getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private static boolean INIT = false;
    private static int STATUS_BAR_HEIGHT = 50;

    private final static String STATUS_BAR_DEF_PACKAGE = "android";
    private final static String STATUS_BAR_DEF_TYPE = "dimen";
    private final static String STATUS_BAR_NAME = "status_bar_height";

    public static synchronized int getStatusBarHeight(final Context context) {
        if (!INIT) {
            int resourceId = context.getResources().
                    getIdentifier(STATUS_BAR_NAME, STATUS_BAR_DEF_TYPE, STATUS_BAR_DEF_PACKAGE);
            if (resourceId > 0) {
                STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
                INIT = true;
                Log.d("StatusBarHeightUtil",
                        String.format("Get status bar height %d", STATUS_BAR_HEIGHT));
            }
        }

        return STATUS_BAR_HEIGHT;
    }
}
