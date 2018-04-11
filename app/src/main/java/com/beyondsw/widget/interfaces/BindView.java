package com.beyondsw.widget.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 绑定默认的数据
 * Created by Administrator on 2018/4/11 0011.
 */

@Retention(RetentionPolicy.RUNTIME)  //标识当前注解存在虚拟机中
@Target(ElementType.FIELD)//定义当前注解使用的范围  这里的使用范围是变量
public @interface BindView {
       int value(); //所有的viewId 都是int  所以用默认的就可以了
}
