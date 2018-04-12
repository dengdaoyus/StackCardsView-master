package com.beyondsw.widget.utlis;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

/**
 * 今日缘份动画工具
 * Created by Administrator on 2018/3/28 0028.
 */

public class TodayFateAdminUtils {


    private static ObjectAnimator animator = null;
    private static ObjectAnimator valueAnimator = null;

    public static void startEndAdmin(View view) {
        if (animator == null) animator = tada(view);
        if (animator.isRunning()) return;
        animator.setRepeatCount(0);
        animator.start();
    }



    public static void onDestoryAdmin() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }


   private static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    private static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -2f * shakeFactor),
                Keyframe.ofFloat(.2f, -2f * shakeFactor),
                Keyframe.ofFloat(.3f, 2f * shakeFactor),
                Keyframe.ofFloat(.4f, -2f * shakeFactor),
                Keyframe.ofFloat(.5f, 2f * shakeFactor),
                Keyframe.ofFloat(.6f, -2f * shakeFactor),
                Keyframe.ofFloat(.7f, 2f * shakeFactor),
                Keyframe.ofFloat(.8f, -2f * shakeFactor),
                Keyframe.ofFloat(.9f, 2f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhRotate).
                setDuration(1000);
    }


    public static void startAdmo(View view) {
        if (valueAnimator == null) valueAnimator = tada2(view);
        if (valueAnimator.isRunning()) return;
        valueAnimator.setRepeatCount(0);
        valueAnimator.setDuration(800);
        valueAnimator.start();
    }

    private static ObjectAnimator tada2( View view) {
        return ObjectAnimator.ofFloat(view, "translationY",
                0f,
                -14f,
                8f,
                0f
        );
    }

}
