package com.beyondsw.widget.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public abstract class LocalImageHolderView implements Holder<Integer> {
    private ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        loadImage(imageView,position,data);
    }
    public abstract void loadImage(ImageView imageView, int position, Integer data);
}