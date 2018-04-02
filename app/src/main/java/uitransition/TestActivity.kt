package uitransition

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.beyondsw.lib.widget.DisplayUtils
import com.beyondsw.widget.R
import kotlinx.android.synthetic.main.layout_details.*

/**
 * Created by Administrator on 2018/4/2 0002.
 */

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_details)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            space.layoutParams.height= DisplayUtils.getStatusBarHeight(this)
            StatusBarUtils().setStatusBarUpperAPI21(this)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarUtils().setStatusBarUpperAPI19(this)
        }

    }


}
