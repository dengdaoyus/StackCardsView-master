package com.beyondsw.widget;

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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.beyondsw.widget.CardFragment.OPTION_IMAGE;

/**
 *
 * Created by Administrator on 2018/3/26 0026.
 */

public class TodayDetailsActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private List<Integer> mList = new ArrayList<>();

    @BindView(R.id.convenientBanner)
    ConvenientBanner<Integer> convenientBanner;
    private int mCurrentPosition = 0, mStartPosition = 0;
    private static final String STATE_CURRENT_PAGE_POSITION = "state_current_page_position";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_details);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        initData();
        mStartPosition = getIntent().getIntExtra("currentTab", 0);
        if (savedInstanceState == null) {
            mCurrentPosition = mStartPosition;
        } else {
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_PAGE_POSITION);
        }

        initBanner();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_PAGE_POSITION, mCurrentPosition);
    }

    private void initData() {
        for (int i = 0; i < ImageUrls.images3.length; i++) {
            mList.add(ImageUrls.images3[i]);
        }
    }

    private void initBanner() {

        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            void loadImage(final ImageView imageView, int position, Integer data) {
                                imageView.setImageResource(data);
                                imageView.setTransitionName(OPTION_IMAGE + position);
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
//        convenientBanner.setManualPageable(false);//设置不能手动影响
        convenientBanner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.setCanLoop(true);
        convenientBanner.setcurrentitem(mCurrentPosition);
        convenientBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
//                setResult(100, new Intent().putExtra("currentTab", mCurrentPosition));
            }
        });
    }

    // @TargetApi(21)
    private void setStartPostTransition(final View sharedView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            startPostponedEnterTransition();
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
    private void stopBanner(){
        if (convenientBanner != null&&convenientBanner.isTurning())
            convenientBanner.stopTurning();
    }

    @Override
    public void finishAfterTransition() {
        stopBanner();
        setResult(100, new Intent().putExtra("currentTab", mCurrentPosition));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterSharedElementCallback(mCallback);
        }
        super.finishAfterTransition();
    }

    private SharedElementCallback mCallback = new SharedElementCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            ImageView mImageView = (ImageView) convenientBanner.getViewPager().getAdapter().instantiateItem(convenientBanner.getViewPager(), convenientBanner.getCurrentItem());
            if (mImageView != null && mStartPosition != mCurrentPosition) {
                names.clear();
                names.add(mImageView.getTransitionName());
                sharedElements.clear();
                sharedElements.put(mImageView.getTransitionName(), mImageView);
            }
        }
    };

}
