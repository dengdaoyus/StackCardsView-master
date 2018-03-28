package com.beyondsw.widget;

import android.annotation.SuppressLint;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyondsw.lib.widget.StackCardsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by
 */
public class CardFragment extends AppCompatActivity implements Handler.Callback, StackCardsView.OnCardSwipedListener
        , View.OnClickListener {
    private StackCardsView mCardsView;
    private CardAdapter mAdapter;
    private HandlerThread mWorkThread;
    private Handler mWorkHandler;
    private Handler mMainHandler;
    private static final int MSG_START_LOAD_DATA = 1;
    private static final int MSG_DATA_LOAD_DONE = 2;
    private volatile int mStartIndex;
    private static final int PAGE_COUNT = 20;

    private ImageView iv_dislike;
    private ImageView iv_like;
    private ImageView iv_follow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taday_fate);
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
        }

    }

    //删除一个卡片
    @Override
    public void onCardDismiss(int direction) {
        mAdapter.remove(0);
        switch (direction) {
            case StackCardsView.SWIPE_LEFT:
                TodayFateAdminUtils.startEndAdmin(iv_dislike);
                break;
            case StackCardsView.SWIPE_RIGHT:
                TodayFateAdminUtils.startEndAdmin(iv_like);
                break;
            case StackCardsView.SWIPE_UP:
                TodayFateAdminUtils.startEndAdmin(iv_follow);
                break;
        }
        if (mAdapter.getCount() < 3) {
            if (!mWorkHandler.hasMessages(MSG_START_LOAD_DATA)) {
                mWorkHandler.obtainMessage(MSG_START_LOAD_DATA).sendToTarget();
            }
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
        if (startIndex < ImageUrls.images.length) {
            final int endIndex = Math.min(mStartIndex + PAGE_COUNT, ImageUrls.images.length - 1);
            List<BaseCardItem> result = new ArrayList<>(endIndex - startIndex + 1);
            for (int i = startIndex; i <= endIndex; i++) {
                ImageCardItem item = new ImageCardItem(this, ImageUrls.images3) {
                    @Override
                    void onEndAnim() {
                        TodayFateAdminUtils.startEndAdmin(mCardsView);
                    }

                    @Override
                    void onTransitionShrink(ImageCardItem imageCardItem, ImageView view, int currentTab) {
                        mImageCardItem = imageCardItem;
                        mImageView = view;
                        startTransitionShrink(view, currentTab);
                    }

                };
                item.dismissDir = StackCardsView.SWIPE_ALL;
                item.fastDismissAllowed = true;
                result.add(item);
            }
            return result;
        }
        return null;
    }


    public static final String OPTION_IMAGE = "image";


    @SuppressLint("RestrictedApi")
    private void startTransitionShrink(View transitionView, int currentTab) {
        Intent intent = new Intent(this, TodayDetailsActivity.class);
        intent.putExtra("currentTab", currentTab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 这里指定了共享的视图元素
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, transitionView, OPTION_IMAGE + currentTab);
            startActivityForResult(intent, 100, options.toBundle());
//            setCallback(currentTab);
        } else {
            startActivity(intent);
        }
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == 100) {
            if (mImageCardItem != null && data != null) {
                exitPosition = data.getIntExtra("currentTab", enterPosition);
                if (exitPosition != -1) {
                    mImageCardItem.loadAvatar(exitPosition, mImageView);
                    mImageCardItem.setCurrentTab(exitPosition);
                }
            }
        }

    }

    private int exitPosition;
    private int enterPosition;

    // @TargetApi(21)
    private void setCallback(final int enterPosition) {
        this.enterPosition = enterPosition;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setExitSharedElementCallback(new SharedElementCallback() {
                // @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    //if (exitPosition != enterPosition && names.size() > 0 && exitPosition < mImageCardItem.getCount()) {
                        names.clear();
                        sharedElements.clear();
                        if(mImageView!=null){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                names.add(mImageView.getTransitionName());
                                sharedElements.put(mImageView.getTransitionName(), mImageView);
                            }
                        }
                  //  }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setExitSharedElementCallback((SharedElementCallback) null);
                    }

                }
            });
        }
    }
}
