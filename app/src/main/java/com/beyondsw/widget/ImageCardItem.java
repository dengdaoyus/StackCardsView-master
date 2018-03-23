package com.beyondsw.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


abstract class ImageCardItem extends BaseCardItem {
    private static final String TAG = "ImageCardItem";
    private String[] adatar;

    ImageCardItem(Context context, String url, String[] adatar) {
        super(context);
        this.adatar = adatar;
    }

    static class ViewHolder {
        ImageView left;
        ImageView right;
        ImageView up;
        ImageView down;
    }

    private int currentTab = 0;

    @Override
    public View getView(View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_imagecard, null);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        final IconPageIndicator iconPageIndicator = (IconPageIndicator) convertView.findViewById(R.id.iconPageIndicator);
        iconPageIndicator.setCurrentCount(adatar.length);
        iconPageIndicator.setCurrentItem(currentTab);
        ImageView left = (ImageView) convertView.findViewById(R.id.left);
        ImageView right = (ImageView) convertView.findViewById(R.id.right);
        ImageView up = (ImageView) convertView.findViewById(R.id.up);
        ImageView down = (ImageView) convertView.findViewById(R.id.down);

        View viewLeft = (View) convertView.findViewById(R.id.viewLeft);
        View viewRight = (View) convertView.findViewById(R.id.viewRight);

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
                if (currentTab == adatar.length - 1) {
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
        return convertView;
    }

    private void loadAvatar(ImageView imageView) {
        Glide.with(mContext)
                .load(adatar[currentTab])
                .placeholder(R.drawable.img_dft)
                .centerCrop()
                .into(imageView);
    }

    abstract void onEndAnim();
}
