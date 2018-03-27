package com.gamesbars.myfavoritememes.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gamesbars.myfavoritememes.Phrase;
import com.gamesbars.myfavoritememes.PhraseAdapter;
import com.gamesbars.myfavoritememes.Phrases;
import com.gamesbars.myfavoritememes.R;

import java.util.List;

import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_FAVORITE;

public class PhraseFragment extends Fragment {

    private TextView toolbarTitle;
    private ImageView toolbarFavorite;
    private SharedPreferences favoritePreferences;
    private boolean isFavorite;

    private MediaPlayer audioPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    releaseMediaPlayer();
                }
            };
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        audioPlayer.stop();
                        releaseMediaPlayer();
                    }
                }
            };

    public PhraseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.phrases_list, container, false);
        final int memeId = getArguments().getInt("id", 1);
        String memeTitle = getArguments().getString("title", "Title");

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //  change current toolbar menu to fragment toolbar menu
        ViewGroup toolbarParent = (ViewGroup) getActivity().findViewById(R.id.toolbar_parent);
        View toolbarMenu = toolbarParent.findViewById(R.id.toolbar_menu);
        if (toolbarMenu != null) {
            toolbarParent.removeView(toolbarMenu);
        }
        View.inflate(getContext(), R.layout.toolbar_phrases, toolbarParent);

        toolbarTitle = (TextView) toolbarParent.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(memeTitle);

        //  favorite functional
        toolbarFavorite = (ImageView) toolbarParent.findViewById(R.id.toolbar_menu);
        favoritePreferences = getActivity()
                .getSharedPreferences(PREFERENCES_FAVORITE, Context.MODE_PRIVATE);
        isFavorite = favoritePreferences.getBoolean(String.valueOf(memeId), false);

        if (isFavorite) toolbarFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        else toolbarFavorite.setImageResource(android.R.drawable.btn_star_big_off);

        toolbarFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite = !isFavorite;

                SharedPreferences.Editor editor = favoritePreferences.edit();
                editor.putBoolean(String.valueOf(memeId), isFavorite);
                editor.apply();

                if (isFavorite)
                    toolbarFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                else toolbarFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            }
        });

        final List<Phrase> phrases = Phrases.getPhrases(memeId);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(new PhraseAdapter(getActivity(), phrases));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  release media player in case another sound playing now
                releaseMediaPlayer();

                //  request audio focus for playback
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //  we have audio focus now
                    //  start playing
                    audioPlayer = MediaPlayer.create(getContext(), phrases.get(position).getSoundId());
                    audioPlayer.start();
                    audioPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (audioPlayer != null) {
            audioPlayer.release();
            audioPlayer = null;
        }
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }
}
