package com.gamesbars.myfavoritememes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class MemeAdapter extends ArrayAdapter<Meme> {

    private SharedPreferences purchasePreferences;

    public MemeAdapter(Activity context, List<Meme> memes, SharedPreferences purchasePreferences) {
        super(context, 0, memes);
        this.purchasePreferences = purchasePreferences;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.memes_list_item, parent, false);
        }

        final Meme currentMeme = getItem(position);

        //  if Meme isn't purchased block it
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_item_image);
        ImageView lockImageView = (ImageView) listItemView.findViewById(R.id.list_item_lock);

        if (currentMeme.isPurchased(purchasePreferences)) {
            imageView.setImageAlpha(255);
            lockImageView.setVisibility(View.GONE);
        } else {
            imageView.setImageAlpha(96);
            lockImageView.setVisibility(View.VISIBLE);
        }

        imageView.setImageResource(currentMeme.getImageResourceId());

        return listItemView;
    }
}

