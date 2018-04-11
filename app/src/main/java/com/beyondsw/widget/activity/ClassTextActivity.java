package com.beyondsw.widget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.beyondsw.widget.R;
import com.beyondsw.widget.bean.UserBean;
import com.beyondsw.widget.interfaces.BindDataKnife;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class ClassTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classtext);
        UserBean userBean = new UserBean();
        BindDataKnife.bind(userBean);
        Log.e("userBean", "获取数据    姓名：" + userBean.getNickname() + "  年龄：" + userBean.getUid() + "  头像：" + userBean.getAvatar());
    }

    private void classTest() {
        //初始化数据

        UserBean userBean = new UserBean("邓道宇", 25, "头像");


        Class userBeanClass = UserBean.class;
        try {
            //类中变量名为私有，需要获取权限
            Field userNameField = userBeanClass.getDeclaredField("nickname");
            userNameField.setAccessible(true);

            Field avatarField = userBeanClass.getField("avatar");

            Field uidField = userBeanClass.getField("uid");

            try {
                String nickname = (String) userNameField.get(userBean);

                String avatar = (String) avatarField.get(userBean);

                int uid = uidField.getInt(userBean);

                Log.e("userBean", "获取数据    姓名：" + nickname + "  年龄：" + uid + "  头像：" + avatar);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            //修改数据源
            try {
                userNameField.set(userBean, "张羽绒");
                Log.e("userBean", "修改数据   姓名：" + userBean.getNickname());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            //获取方法

            Method[] methods = userBeanClass.getMethods();
            for (Method method : methods
                    ) {
                Log.e("userBean", "获取方法   姓名：" + method.getName());
            }


            //获取方法并设置值
            try {
                Method method = userBeanClass.getMethod("setUserInfo", String.class);
                try {
                    method.invoke(userBean, "唐诗");
                    Log.e("userBean", "获取修改后的昵称   姓名：" + userBean.getNickname());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
