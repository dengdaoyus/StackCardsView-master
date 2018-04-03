package com.beyondsw.widget.swipecard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.beyondsw.lib.widget.swipecard.SwipeCardView;
import com.beyondsw.widget.R;
import com.beyondsw.widget.activity.TodayDetailsActivity;
import com.beyondsw.widget.utlis.ImageUrls;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.beyondsw.widget.activity.CardFragment.OPTION_IMAGE;


public class CardSwipeActivity extends AppCompatActivity {

    private ArrayList<Integer> al;
    private CardsAdapter arrayAdapter;

    @BindView(R.id.card_stack_view)
    public SwipeCardView swipeCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);
        al = new ArrayList<>();

        arrayAdapter = new CardsAdapter(this, al);
        swipeCardView.setAdapter(arrayAdapter);
        swipeCardView.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(Object dataObject) {
                makeToast(CardSwipeActivity.this, "Left!");
            }

            @Override
            public void onCardExitRight(Object dataObject) {
                makeToast(CardSwipeActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

            @Override
            public void onCardExitTop(Object dataObject) {
                makeToast(CardSwipeActivity.this, "Top!");
            }

            @Override
            public void onCardExitBottom(Object dataObject) {
                makeToast(CardSwipeActivity.this, "Bottom!");
            }
        });

        // Optionally add an OnItemClickListener
        swipeCardView.setOnItemClickListener(new SwipeCardView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(CardSwipeActivity.this, String.valueOf(swipeCardView.getCurrentPosition()));

                startTransitionShrink(swipeCardView.getSelectedView().findViewById(R.id.image),swipeCardView.getCurrentPosition());
            }
        });
        al.addAll(Arrays.asList(ImageUrls.images3));
        arrayAdapter.notifyDataSetChanged();
    }


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


    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.top)
    public void top() {
        /**
         * Trigger the right event manually.
         */
        swipeCardView.throwTop();
    }

    @OnClick(R.id.bottom)
    public void bottom() {
        swipeCardView.throwBottom();
    }

    @OnClick(R.id.left)
    public void left() {
        swipeCardView.throwLeft();
    }

    @OnClick(R.id.right)
    public void right() {
        swipeCardView.throwRight();
    }

    @OnClick(R.id.restart)
    public void restart() {
        swipeCardView.restart();
    }

    @OnClick(R.id.position)
    public void toastCurrentPosition() {
        makeToast(this, String.valueOf(swipeCardView.getCurrentPosition()));
    }
}
