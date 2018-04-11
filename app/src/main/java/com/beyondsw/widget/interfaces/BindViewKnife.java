package com.beyondsw.widget.interfaces;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.beyondsw.widget.R;
import com.beyondsw.widget.bean.UserBean;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class BindViewKnife {

    public static void bind(Activity activity) {
        try {
            bindView(activity);
        } catch (Exception e) {
            Log.e("bind", "bind 注解为空");
        }
    }

    private static void bindView(Activity activity) {
        Class<? extends Activity> userBeanClass = activity.getClass();
        //Activity 中的变量
        Field[] fields = userBeanClass.getDeclaredFields();
        for (Field field : fields
                ) {
            //全部反暴力
            field.setAccessible(true);
            //获取注解
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                int id = bindView.value();
                View view = activity.findViewById(id);
                try {
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("BindViewKnife", "bindView 注解为空");
            }
        }
    }
}
