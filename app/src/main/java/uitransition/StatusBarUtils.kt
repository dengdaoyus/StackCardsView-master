package uitransition

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.beyondsw.lib.widget.DisplayUtils
import com.beyondsw.widget.R

/**
 * Created by Administrator on 2018/4/2 0002.
 */

internal class StatusBarUtils {
    fun setStatusBarUpperAPI19(activity: Activity) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val mContentView = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val statusBarHeight = DisplayUtils.getStatusBarHeight(activity)
        val statusColor = ContextCompat.getColor(activity, R.color.colorPrimary)

        var mTopView: View? = mContentView.getChildAt(0)
        if (mTopView != null && mTopView.layoutParams != null &&
                mTopView.layoutParams.height == statusBarHeight) {
            //避免重复添加 View
            mTopView.setBackgroundColor(statusColor)
            return
        }
        //使 ChildView 预留空间
        if (mTopView != null) {
            ViewCompat.setFitsSystemWindows(mTopView, true)
        }

        //添加假 View
        mTopView = View(activity)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
        mTopView.setBackgroundColor(statusColor)
        mContentView.addView(mTopView, 0, lp)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarUpperAPI21(activity: Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        //由于setStatusBarColor()这个API最低版本支持21, 本人的是15,所以如果要设置颜色,自行到style中通过配置文件设置
        window.statusBarColor = Color.parseColor("#80000000")

    }
}
