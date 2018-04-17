package com.beyondsw.widget.activity;

import android.annotation.SuppressLint;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.beyondsw.lib.widget.StackCardsView;
import com.beyondsw.widget.BaseCardItem;
import com.beyondsw.widget.CardAdapter;
import com.beyondsw.widget.ImageCardItem;
import com.beyondsw.widget.R;
import com.beyondsw.widget.utlis.ImageUrls;
import com.beyondsw.widget.utlis.TodayFateAdminUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uitransition.MainActivity;

/**
 * Created by
 */
public class CardFragment extends AppCompatActivity implements Handler.Callback, StackCardsView.OnCardSwipedListener
        , View.OnClickListener, ImageCardItem.ImageCardCallBack {
    private StackCardsView mCardsView;
    private CardAdapter mAdapter;
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    private Handler mMainHandler;
    private static final int MSG_START_LOAD_DATA = 1;
    private static final int MSG_DATA_LOAD_DONE = 2;
    private volatile int mStartIndex = 0;
    private static final int PAGE_COUNT = 5;

    private ImageView iv_dislike;
    private ImageView iv_like;
    private ImageView iv_follow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taday_fate);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setExitSharedElementCallback(mCallback);
        }
        initView();
    }

    private void initView() {

        iv_dislike = (ImageView) findViewById(R.id.iv_dislike);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        iv_follow = (ImageView) findViewById(R.id.iv_follow);

        iv_dislike.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        iv_follow.setOnClickListener(this);

        mCardsView = (StackCardsView) findViewById(R.id.cards);
        mCardsView.addOnCardSwipedListener(this);
        mAdapter = new CardAdapter();
        mCardsView.setAdapter(mAdapter);
        mMainHandler = new Handler(this);
        mWorkThread = new HandlerThread("data_loader");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper(), this);
        mWorkHandler.obtainMessage(MSG_START_LOAD_DATA).sendToTarget();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TodayFateAdminUtils.onDestoryAdmin();
        mCardsView.removeOnCardSwipedListener(this);

        mWorkThread.quit();
        mWorkHandler.removeMessages(MSG_START_LOAD_DATA);
        mMainHandler.removeMessages(MSG_DATA_LOAD_DONE);
        mStartIndex = 0;
    }


    @Override
    public void onClick(View v) {
        if (v == iv_dislike) {
            mCardsView.removeCover(StackCardsView.SWIPE_LEFT);
        } else if (v == iv_like) {
            mCardsView.removeCover(StackCardsView.SWIPE_RIGHT);
        } else if (v == iv_follow) {
            mCardsView.removeCover(StackCardsView.SWIPE_UP);
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    //删除一个卡片
    @Override
    public void onCardDismiss(int direction) {
        mAdapter.remove(0);
        switch (direction) {
            case StackCardsView.SWIPE_LEFT:
                Log.e("onCardDismiss", "SWIPE_LEFT");
                TodayFateAdminUtils.startEndAdmin(iv_dislike);
                break;
            case StackCardsView.SWIPE_RIGHT:
                Log.e("onCardDismiss", "SWIPE_RIGHT");
                TodayFateAdminUtils.startEndAdmin(iv_like);
                break;
            case StackCardsView.SWIPE_UP:
                Log.e("onCardDismiss", "SWIPE_UP");
                TodayFateAdminUtils.startEndAdmin(iv_follow);
                break;
            case StackCardsView.SWIPE_DOWN:
                Log.e("onCardDismiss", "SWIPE_DOWN");
                break;
        }
    }

    @Override
    public void onCardScrolled(View view, float progress, int direction) {
        Object tag = view.getTag();
        if (tag instanceof ImageCardItem.ViewHolder) {
            ImageCardItem.ViewHolder vh = (ImageCardItem.ViewHolder) tag;
            if (progress > 0) {
                switch (direction) {
                    case StackCardsView.SWIPE_LEFT:
                        vh.left.setAlpha(progress);
                        vh.right.setAlpha(0f);
                        vh.up.setAlpha(0f);
                        vh.down.setAlpha(0f);
                        break;
                    case StackCardsView.SWIPE_RIGHT:
                        vh.right.setAlpha(progress);
                        vh.left.setAlpha(0f);
                        vh.up.setAlpha(0f);
                        vh.down.setAlpha(0f);
                        break;
                    case StackCardsView.SWIPE_UP:
                        vh.up.setAlpha(progress);
                        vh.left.setAlpha(0f);
                        vh.right.setAlpha(0f);
                        vh.down.setAlpha(0f);
                        break;
                    case StackCardsView.SWIPE_DOWN:
                        vh.down.setAlpha(progress);
                        vh.left.setAlpha(0f);
                        vh.right.setAlpha(0f);
                        vh.up.setAlpha(0f);
                        break;
                }
            } else {
                vh.left.setAlpha(0f);
                vh.right.setAlpha(0f);
                vh.up.setAlpha(0f);
                vh.down.setAlpha(0f);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_START_LOAD_DATA: {
                List<BaseCardItem> data = loadData(mStartIndex);
                mMainHandler.obtainMessage(MSG_DATA_LOAD_DONE, data).sendToTarget();
                break;
            }
            case MSG_DATA_LOAD_DONE: {
                List<BaseCardItem> data = (List<BaseCardItem>) msg.obj;
                mAdapter.appendItems(data);
                mStartIndex += sizeOfImage(data);
                break;
            }
        }
        return true;
    }

    private int sizeOfImage(List<BaseCardItem> items) {
        if (items == null) {
            return 0;
        }
        int size = 0;
        for (BaseCardItem item : items) {
            if (item instanceof ImageCardItem) {
                size++;
            }
        }
        return size;
    }

    private ImageCardItem mImageCardItem = null;
    private ImageView mImageView = null;

    private List<BaseCardItem> loadData(int startIndex) {
        List<BaseCardItem> result = new ArrayList<>();
        for (int i = startIndex; i < ImageUrls.images4.length; i++) {
            ImageCardItem item = new ImageCardItem(this, ImageUrls.images4, i, this);
            item.dismissDir = StackCardsView.SWIPE_ALL2;
            item.fastDismissAllowed = false;
            result.add(item);
        }
        return result;
    }


    public static final String OPTION_IMAGE = "image";


    @SuppressLint("RestrictedApi")
    private void startTransitionShrink(ImageView transitionView, ImageView row, int currentTab) {
        TodayDetailsActivity.start(this, transitionView, row, currentTab);
    }

    int startingPosition;
    int currentPosition;

    @Override
    public void onActivityReenter(int requestCode, Intent data) {
        super.onActivityReenter(requestCode, data);
        Bundle mTmpReenterState = data.getExtras();
        startingPosition = mTmpReenterState.getInt("startingPosition");
        currentPosition = mTmpReenterState.getInt("currentPosition");
        if (startingPosition != currentPosition && mImageCardItem != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                postponeEnterTransition();
//            }
            mImageCardItem.setCurrentTab(currentPosition);
            mImageCardItem.loadAvatar2(currentPosition, mImageView);
        }
    }


    private final SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (startingPosition != currentPosition && mImageView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    names.clear();
                    names.add(OPTION_IMAGE+currentPosition);
                    sharedElements.clear();
                    sharedElements.put(OPTION_IMAGE+currentPosition, mImageView);

                }
            }
        }
    };


    @Override
    public void onEndAnim() {
        TodayFateAdminUtils.startEndAdmin(mCardsView);
    }

    @Override
    public void onTransitionShrink(ImageCardItem imageCardItem, ImageView view, ImageView row, int startCurrentTab) {
        mImageCardItem = imageCardItem;
        mImageView = view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.e("name", "name0:  " + mImageView.getTransitionName());
        }
        startTransitionShrink(view, row, startCurrentTab);
    }

    @Override
    public void onStartPostponedEnterTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startPostponedEnterTransition();
        }
    }

}
