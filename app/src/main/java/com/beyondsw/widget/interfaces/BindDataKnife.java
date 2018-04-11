package com.beyondsw.widget.interfaces;

import android.util.Log;

import com.beyondsw.widget.bean.UserBean;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class BindDataKnife {

    public static void bind(UserBean userBean) {
        Class userBeanClass = userBean.getClass();
        try {
            Field fieldNickName = userBeanClass.getDeclaredField("nickname");
            fieldNickName.setAccessible(true);
            Field fieldAvatar = userBeanClass.getDeclaredField("avatar");
            fieldNickName.setAccessible(true);
            Field fieldUid = userBeanClass.getField("uid");
            //通过反色获取注解
            BindData bindData = fieldNickName.getAnnotation(BindData.class);
            if (bindData != null) {
                String nickname = bindData.nickname();
                int age = bindData.age();
                String avatar = bindData.avatar();
                //修改数据源
                try {
                    fieldNickName.set(userBean, nickname);
                    fieldAvatar.set(userBean, avatar);
                    fieldUid.set(userBean, age);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("BindDataKnife", "注解为空");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


    }
}
