package com.beyondsw.widget.bean;

import android.util.Log;

import com.beyondsw.widget.interfaces.BindData;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class UserBean
{
    @BindData(nickname="我是谁",age=21,avatar ="头像")
    private  String  nickname;
    public int uid;
    public String avatar;

    public UserBean() {
    }

    public UserBean(String nickname, int uid, String avatar) {
        this.nickname = nickname;
        this.uid = uid;
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUserInfo(String  nickname){

        Log.e("userBean","信息：原昵称 ："+this.nickname);
        Log.e("userBean"," 修改后昵称："+(this.nickname=nickname));

    }
}
