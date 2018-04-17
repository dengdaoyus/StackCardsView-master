package com.beyondsw.widget;

import android.content.Context;
import android.os.Build;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import static com.beyondsw.widget.activity.CardFragment.OPTION_IMAGE;


public class ImageCardItem extends BaseCardItem {
    private Integer[] avatar;
    private int currentTab = 0;
    private int prosion = 0;

    public ImageCardItem(Context context, Integer[] avatar, int i,@NotNull ImageCardCallBack imageCardCallBack) {
        super(context);
        this.avatar = avatar;
        this.prosion = i;
        this.imageCardCallBack=imageCardCallBack;
    }

    public static class ViewHolder {
        public ImageView left;
        public ImageView right;
        public ImageView up;
        public ImageView down;
    }

    ImageView imageView;
    IconPageIndicator iconPageIndicator;

    @Override
    public View getView(View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_imagecard, null);
        imageView = convertView.findViewById(R.id.image);
        iconPageIndicator = convertView.findViewById(R.id.iconPageIndicator);
        iconPageIndicator.setCurrentCount(avatar.length);
        iconPageIndicator.setCurrentItem(currentTab);
        ImageView left = convertView.findViewById(R.id.left);
        ImageView right = convertView.findViewById(R.id.right);
        ImageView up = convertView.findViewById(R.id.up);
        ImageView down = convertView.findViewById(R.id.down);
        View viewLeft = convertView.findViewById(R.id.viewLeft);
        View viewRight = convertView.findViewById(R.id.viewRight);

        ImageView iv_up = convertView.findViewById(R.id.iv_up);
        final ImageView iv_row = convertView.findViewById(R.id.iv_up);
        ViewHolder vh = new ViewHolder();
        vh.left = left;
        vh.right = right;
        vh.up = up;
        vh.down = down;
        convertView.setTag(vh);
        loadAvatar(currentTab, imageView);
        viewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTab == avatar.length - 1) {
                    imageCardCallBack.onEndAnim();
                } else {
                    currentTab++;
                    loadAvatar(currentTab, imageView);
                    iconPageIndicator.setCurrentItem(currentTab);
                }
            }
        });
        viewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTab < 0) {
                    currentTab = 0;
                    loadAvatar(currentTab, imageView);
                    iconPageIndicator.setCurrentItem(currentTab);
                } else if (currentTab > 0) {
                    currentTab--;
                    loadAvatar(currentTab, imageView);
                    iconPageIndicator.setCurrentItem(currentTab);
                }
            }
        });
        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setTransitionName(OPTION_IMAGE + currentTab);
                    iv_row.setTransitionName(OPTION_IMAGE + "row" + prosion);
                }
                imageCardCallBack.onTransitionShrink(ImageCardItem.this, imageView, iv_row, currentTab);
            }
        });
        return convertView;
    }


    public void loadAvatar(int currentTab, ImageView imageView) {
        imageView.setImageResource(avatar[currentTab]);
    }


    public void loadAvatar2(int currentTab, ImageView imageView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(OPTION_IMAGE + currentTab);
        }
        imageView.setImageResource(avatar[currentTab]);
        imageCardCallBack.onStartPostponedEnterTransition();
    }

    public void setCurrentTab(int currentTab) {
        this.currentTab = currentTab;
        iconPageIndicator.setCurrentItem(currentTab);
    }

    int getCount() {
        return avatar.length;
    }


    private ImageCardCallBack imageCardCallBack;


    public interface  ImageCardCallBack{
       void  onEndAnim();
        void onTransitionShrink(ImageCardItem imageCardItem, ImageView view, ImageView row, int currentTab);
        void onStartPostponedEnterTransition();
    }
}
