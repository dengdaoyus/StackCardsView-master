package com.beyondsw.widget;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beyondsw.lib.widget.StackCardsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wensefu on 17-2-4.
 */
public class CardFragment extends Fragment implements Handler.Callback, StackCardsView.OnCardSwipedListener
        , View.OnClickListener {

    private static final String TAG = "StackCardsView-DEMO";

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

    private ImageView iv_up;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_taday_fate, null);

        iv_dislike = (ImageView) root.findViewById(R.id.iv_dislike);
        iv_like = (ImageView) root.findViewById(R.id.iv_like);
        iv_follow = (ImageView) root.findViewById(R.id.iv_follow);
        iv_up = (ImageView) root.findViewById(R.id.iv_up);
        iv_dislike.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        iv_follow.setOnClickListener(this);
        iv_up.setOnClickListener(this);
        mCardsView = (StackCardsView) root.findViewById(R.id.cards);
        mCardsView.addOnCardSwipedListener(this);
        mAdapter = new CardAdapter();
        mCardsView.setAdapter(mAdapter);
        mMainHandler = new Handler(this);
        mWorkThread = new HandlerThread("data_loader");
        mWorkThread.start();
        mWorkHandler = new Handler(mWorkThread.getLooper(), this);
        mWorkHandler.obtainMessage(MSG_START_LOAD_DATA).sendToTarget();

        return root;
    }

    private void startEndAdmin() {
        ObjectAnimator animator = tada(mCardsView);
        animator.setRepeatCount(0);
        animator.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        }else  if(v==iv_up){

        }
    }

    @Override
    public void onCardDismiss(int direction) {
        mAdapter.remove(0);
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


    private List<BaseCardItem> loadData(int startIndex) {
        if (startIndex < ImageUrls.images.length) {
            final int endIndex = Math.min(mStartIndex + PAGE_COUNT, ImageUrls.images.length - 1);
            List<BaseCardItem> result = new ArrayList<>(endIndex - startIndex + 1);
            for (int i = startIndex; i <= endIndex; i++) {
                ImageCardItem item = new ImageCardItem(getActivity(), ImageUrls.images[i],ImageUrls.images2) {
                    @Override
                    void onEndAnim() {
                        startEndAdmin();
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

    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -2f * shakeFactor),
                Keyframe.ofFloat(.2f, -2f * shakeFactor),
                Keyframe.ofFloat(.3f, 2f * shakeFactor),
                Keyframe.ofFloat(.4f, -2f * shakeFactor),
                Keyframe.ofFloat(.5f, 2f * shakeFactor),
                Keyframe.ofFloat(.6f, -2f * shakeFactor),
                Keyframe.ofFloat(.7f, 2f * shakeFactor),
                Keyframe.ofFloat(.8f, -2f * shakeFactor),
                Keyframe.ofFloat(.9f, 2f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhRotate).
                setDuration(1000);
    }

}
