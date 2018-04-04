package com.beyondsw.widget.swipecard;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.beyondsw.widget.R;
import com.beyondsw.widget.RoundImageView;

import java.util.ArrayList;

import static com.beyondsw.widget.activity.CardFragment.OPTION_IMAGE;


public class CardsAdapter extends ArrayAdapter<Integer> {
    private final ArrayList<Integer> cards;
    private final LayoutInflater layoutInflater;



    public CardsAdapter(Context context, ArrayList<Integer> cards) {
        super(context, -1);
        this.cards = cards;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.item_imagecard, parent,false);
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageResource(cards.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setTransitionName(OPTION_IMAGE + position);
        }
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(),"imageView", Toast.LENGTH_SHORT).show();
//            }
//        });
        return view;
    }

    @Override
    public Integer getItem(int position) {
        return cards.get(position);
    }


    @Override
    public int getCount() {
        return cards.size();
    }
}
