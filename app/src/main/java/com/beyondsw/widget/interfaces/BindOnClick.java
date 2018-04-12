package com.beyondsw.widget.interfaces;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class BindOnClick {

    public static void bind(Activity activity) {
        try {
            bindView(activity);
        } catch (Exception e) {
            Log.e("bind", "bind 注解为空");
        }
    }

    private static void bindView(final Activity activity) {
        Class<? extends Activity> userBeanClass = activity.getClass();
        //Activity 中的变量
        Method[] methods = userBeanClass.getMethods();
        for (final Method method : methods
                ) {
            //获取注解
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int id = onClick.value();
                View view = activity.findViewById(id);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            method.invoke(activity,"");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Log.e("BindViewKnife", "bindView 注解为空");
            }
        }
    }
}
