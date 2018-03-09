package com.gamesbars.myfavoritememes.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gamesbars.myfavoritememes.PhraseAdapter;
import com.gamesbars.myfavoritememes.Phrases;
import com.gamesbars.myfavoritememes.R;

public class MemeFragment extends Fragment {

    private TextView toolbarTitle;
    private ImageView toolbarFavorite;

    public MemeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.phrases_list, container, false);
        int memeId = getArguments().getInt("id", 1);

        //  change current toolbar menu to fragment toolbar menu
        ViewGroup toolbarParent = (ViewGroup) getActivity().findViewById(R.id.toolbar_parent);
        View toolbarMenu = toolbarParent.findViewById(R.id.toolbar_menu);
        if (toolbarMenu != null) {
            toolbarParent.removeView(toolbarMenu);
        }
        View.inflate(getContext(), R.layout.toolbar_phrases, toolbarParent);

        toolbarTitle = (TextView) toolbarParent.findViewById(R.id.toolbar_title);
        toolbarFavorite = (ImageView) toolbarParent.findViewById(R.id.toolbar_menu);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(new PhraseAdapter(getActivity(), Phrases.getPhrases(memeId)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  ...play sound
            }
        });

        return rootView;
    }
}
