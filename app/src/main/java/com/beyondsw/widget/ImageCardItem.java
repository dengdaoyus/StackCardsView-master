package com.beyondsw.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static com.beyondsw.widget.CardFragment.OPTION_IMAGE;


abstract class ImageCardItem extends BaseCardItem {
    private int[] avatar;
    private int currentTab = 0;

    ImageCardItem(Context context, int[] avatar) {
        super(context);
        this.avatar = avatar;
    }

    static class ViewHolder {
        ImageView left;
        ImageView right;
        ImageView up;
        ImageView down;
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
                    onEndAnim();
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
                onTransitionShrink(ImageCardItem.this, imageView, currentTab);
            }
        });
        return convertView;
    }


    void loadAvatar(int currentTab, ImageView imageView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(OPTION_IMAGE + currentTab);
        }
        imageView.setImageResource(avatar[currentTab]);
        //        Glide.with(mContext)
//                .load(adatar[currentTab])
//                .placeholder(R.drawable.img_dft)
//                .centerCrop()
//                .into(imageView);
    }

    void setCurrentTab(int currentTab) {
        this.currentTab=currentTab;
        iconPageIndicator.setCurrentItem(currentTab);
    }
    int  getCount(){
        return avatar.length;
    }

    abstract void onEndAnim();

    //转场收缩
    abstract void onTransitionShrink(ImageCardItem imageCardItem, ImageView view, int currentTab);
}
