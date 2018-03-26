package com.beyondsw.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

    @Override
    public View getView(View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_imagecard, null);
        final ImageView imageView = convertView.findViewById(R.id.image);
        final IconPageIndicator iconPageIndicator = convertView.findViewById(R.id.iconPageIndicator);
        iconPageIndicator.setCurrentCount(avatar.length);
        iconPageIndicator.setCurrentItem(currentTab);
        ImageView left = convertView.findViewById(R.id.left);
        ImageView right = convertView.findViewById(R.id.right);
        ImageView up = convertView.findViewById(R.id.up);
        ImageView down = convertView.findViewById(R.id.down);
        // 这里指定了被共享的视图元素
        ViewCompat.setTransitionName(imageView, "image");
        View viewLeft = convertView.findViewById(R.id.viewLeft);
        View viewRight = convertView.findViewById(R.id.viewRight);

        ImageView iv_up=convertView.findViewById(R.id.iv_up);

        ViewHolder vh = new ViewHolder();
        vh.left = left;
        vh.right = right;
        vh.up = up;
        vh.down = down;
        convertView.setTag(vh);
        loadAvatar(imageView);
        viewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTab == avatar.length - 1) {
                    onEndAnim();
                } else {
                    currentTab++;
                    loadAvatar(imageView);
                    iconPageIndicator.setCurrentItem(currentTab);
                }
            }
        });
        viewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTab < 0) {
                    currentTab = 0;
                    loadAvatar(imageView);
                    iconPageIndicator.setCurrentItem(currentTab);
                } else if (currentTab > 0) {
                    currentTab--;
                    loadAvatar(imageView);
                    iconPageIndicator.setCurrentItem(currentTab);
                }
            }
        });
        iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTransitionShrink(imageView,currentTab);
            }
        });
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadAvatar(ImageView imageView) {
//        Glide.with(mContext)
//                .load(adatar[currentTab])
//                .placeholder(R.drawable.img_dft)
//                .centerCrop()
//                .into(imageView);
        imageView.setTransitionName(OPTION_IMAGE+currentTab);
        imageView.setImageResource(avatar[currentTab]);
    }

    abstract void onEndAnim();

    //转场收缩
    abstract void onTransitionShrink(View view,int currentTab);
}
