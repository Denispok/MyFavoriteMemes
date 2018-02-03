package com.gamesbars.myfavoritememes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Meme> {

    private Context mContext;
    private SharedPreferences purchasePreferences;

    public ImageAdapter(Activity context, ArrayList<Meme> memes, SharedPreferences purchasePreferences) {
        super(context, 0, memes);
        mContext = context;
        this.purchasePreferences = purchasePreferences;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Meme currentMeme = getItem(position);

        //  if Meme isn't purchased block it
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_item_image);
        ImageView lockImageView = (ImageView) listItemView.findViewById(R.id.list_item_lock);

        if (currentMeme.checkPurchase(purchasePreferences)) {
            imageView.setImageAlpha(96);
            lockImageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setImageAlpha(255);
            lockImageView.setVisibility(View.GONE);
        }

        imageView.setImageResource(currentMeme.getImageResourceId());

        return listItemView;
    }
}
