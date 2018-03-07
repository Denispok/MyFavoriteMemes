package com.gamesbars.myfavoritememes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PhraseAdapter extends ArrayAdapter<Phrase> {

    public PhraseAdapter(Activity context, ArrayList<Phrase> phrases) {
        super(context, 0, phrases);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.phrases_list_item, parent, false);
        }

        final Phrase currentPhrase = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.list_item_text);
        textView.setText(currentPhrase.getText());

        return listItemView;
    }
}
