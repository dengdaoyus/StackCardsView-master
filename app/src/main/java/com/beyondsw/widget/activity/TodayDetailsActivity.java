package com.beyondsw.widget.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.beyondsw.widget.R;
import com.beyondsw.widget.utlis.ImageUrls;
import com.beyondsw.widget.utlis.TodayFateAdminUtils;
import com.beyondsw.widget.view.LocalImageHolderView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.beyondsw.widget.activity.CardFragment.OPTION_IMAGE;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class TodayDetailsActivity extends AppCompatActivity {

    private List<Integer> mList = new ArrayList<>();

    @BindView(R.id.convenientBanner)
    ConvenientBanner<Integer> convenientBanner;
    @BindView(R.id.iv_up)
    ImageView mFloatingActionButton;

    private int mCurrentPosition = 0, mStartPosition = 0;


    public static void start(@NonNull Activity activity, @NonNull ImageView mAvatar, @NonNull ImageView mRow,  int position) {
        final Intent intent = new Intent(activity, TodayDetailsActivity.class);
        intent.putExtra("startingPosition", position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.putExtra("rowTransitionName", mRow.getTransitionName());
            Pair titlePair = Pair.create(mRow, mRow.getTransitionName());
            Pair iconPair = Pair.create(mAvatar, mAvatar.getTransitionName());
            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                        titlePair, iconPair);
            activity.startActivity(intent, options.toBundle());
        }else {
            activity.startActivity(intent);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_details);
        ButterKnife.bind(this);
        mStartPosition = getIntent().getIntExtra("startingPosition", 0);
        mCurrentPosition = mStartPosition;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFloatingActionButton.setTransitionName(getIntent().getStringExtra("rowTransitionName"));
            postponeEnterTransition();
            setEnterSharedElementCallback(mCallback);
        }
        initData();
        initBanner();
    }

    private void initData() {
        for (int i = 0; i < ImageUrls.images4.length; i++) {
            mList.add(ImageUrls.images4[i]);
        }
    }

    private void initBanner() {
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView() {
                            @Override
                            public void loadImage(final ImageView imageView, int position, Integer data) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    imageView.setTransitionName(OPTION_IMAGE + position);

                                }
                                imageView.setImageResource(data);
                                if (mStartPosition == position) {
                                    new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                    startPostponedEnterTransition();
                                                }
                                            }
                                        },300);

                                }
                            }
                        };
                    }
                }, mList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        //设置翻页的效果，不需要翻页效果可用不设
        convenientBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.setCanLoop(false);
        convenientBanner.setcurrentitem(mStartPosition);
        convenientBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
                Log.e("name", "mCurrentPosition   :  " +mCurrentPosition);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (convenientBanner != null && !convenientBanner.isTurning())
            convenientBanner.startTurning(3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopBanner();
    }

    private void stopBanner() {
        if (convenientBanner != null && convenientBanner.isTurning())
            convenientBanner.stopTurning();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finishAfterTransition() {
        setActivityResult();
        super.finishAfterTransition();
    }

    private void setActivityResult() {
        stopBanner();

        Log.e("name", "setActivityResult   :  mStartPosition  " +mStartPosition +"    mCurrentPosition   "+mCurrentPosition);
        setResult(RESULT_OK, new Intent().putExtra("startingPosition", mStartPosition).putExtra("currentPosition", mCurrentPosition));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        TodayFateAdminUtils.onDestoryAdmin();
    }

    private SharedElementCallback mCallback = new SharedElementCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            ImageView mImageView = (ImageView) convenientBanner.getViewPager().getAdapter().instantiateItem(convenientBanner.getViewPager(), mCurrentPosition);
            if (mImageView != null && mStartPosition != mCurrentPosition) {
                names.clear();
                names.add(mImageView.getTransitionName());
                sharedElements.clear();
                sharedElements.put(mImageView.getTransitionName(), mImageView);
            }
        }
    };
}
