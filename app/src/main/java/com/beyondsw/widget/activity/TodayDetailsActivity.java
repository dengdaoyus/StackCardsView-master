package com.beyondsw.widget.activity;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
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
    private Handler mHandler = new Handler();
    private List<Integer> mList = new ArrayList<>();

    @BindView(R.id.convenientBanner)
    ConvenientBanner<Integer> convenientBanner;


    @BindView(R.id.mFloatingActionButton)
    FloatingActionButton mFloatingActionButton;

    private int mCurrentPosition = 0, mStartPosition = 0;
    private static final String STATE_CURRENT_PAGE_POSITION = "state_current_page_position";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_details);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        mStartPosition = getIntent().getIntExtra("currentTab", 0);
        mCurrentPosition = mStartPosition;

        initData();
        initBanner();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setInterpolator(new AccelerateInterpolator());
            getWindow().getEnterTransition().setDuration(100);
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    TodayFateAdminUtils.startAdmo(mFloatingActionButton);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }

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
                                imageView.setImageResource(data);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    imageView.setTransitionName(OPTION_IMAGE + position);
                                }
                                if (mStartPosition == position) {
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            setStartPostTransition(imageView);
                                        }
                                    }, 200);
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
        //.setPageTransformer(Transformer.DefaultTransformer);    集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
//        convenientBanner.setManualPageable(true);//设置不能手动影响
        convenientBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.setCanLoop(false);
        convenientBanner.setcurrentitem(mStartPosition);
        convenientBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPosition = position;
            }
        });


    }


    // @TargetApi(21)
    private void setStartPostTransition(final View sharedView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            startPostponedEnterTransition();
        convenientBanner.setCanLoop(true);
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
    public void finishAfterTransition() {
        stopBanner();
        setResult(100, new Intent().putExtra("currentTab", mCurrentPosition));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convenientBanner.setCanLoop(false);
            setEnterSharedElementCallback(mCallback);
        }
        super.finishAfterTransition();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TodayFateAdminUtils.onDestoryAdmin();
    }
}
