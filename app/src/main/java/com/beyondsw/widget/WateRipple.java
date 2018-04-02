package com.beyondsw.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
/**
 * Created by admin on 2016/12/29.
 */
public class WateRipple extends View {
    private Paint mPaint;
    public WateRipple(Context context, Paint paint){
        super(context);
        if(paint==null){
            this.mPaint = new Paint();
        }else{
            this.mPaint = paint;
        }
        setVisibility(View.INVISIBLE);//刚开始设置不可见
    }
    public WateRipple(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius=(Math.min(getWidth(),getHeight()))/2;
        canvas.drawCircle(radius,radius,radius,mPaint);
    }
}
