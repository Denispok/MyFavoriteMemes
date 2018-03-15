package com.gamesbars.myfavoritememes.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.gamesbars.myfavoritememes.ImageAdapter;
import com.gamesbars.myfavoritememes.Meme;
import com.gamesbars.myfavoritememes.R;

import java.util.ArrayList;

import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_APP;
import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_APP_COINS;
import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_FAVORITE;
import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_PURCHASE;

public class ListFragment extends Fragment {

    private final static Integer START_COINS = 1000;  //  start count of coins
    private final static Integer MEME_PRICE = 100;  //   price of one meme
    private final static Integer CLASSIC_COUNT = 16;
    private final static Integer CLASSIC_OPEN_COUNT = 2; // initially purchased classic memes
    private final static Integer GAMES_COUNT = 16;
    private final static Integer GAMES_OPEN_COUNT = 2;   // initially purchased games memes

    public State state;
    private ArrayList<Meme> classic;
    private ArrayList<Meme> games;
    private ArrayList<Meme> favorite;

    private GridView gridView;
    private ImageAdapter imageAdapter;

    private TextView toolbarTitle;
    private TextView toolbarCoins;

    private AlertDialog purchaseDialog;
    private Integer clickedMemeId;
    private AlertDialog dontEnoughCoinsDialog;

    private SharedPreferences appPreferences;
    private SharedPreferences purchasedPreferences;
    private SharedPreferences favoritePreferences;
    private SharedPreferences.Editor editor;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = State.CLASSIC;
        initializePreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.memes_list, container, false);

        //  change current toolbar menu to fragment toolbar menu
        ViewGroup toolbarParent = (ViewGroup) getActivity().findViewById(R.id.toolbar_parent);
        View toolbarMenu = toolbarParent.findViewById(R.id.toolbar_menu);
        if (toolbarMenu != null) {
            toolbarParent.removeView(toolbarMenu);
        }
        View.inflate(getContext(), R.layout.toolbar_memes, toolbarParent);

        toolbarTitle = (TextView) toolbarParent.findViewById(R.id.toolbar_title);
        toolbarCoins = (TextView) toolbarParent.findViewById(R.id.toolbar_menu);
        refreshCoins(0);

        gridView = (GridView) rootView.findViewById(R.id.grid_view);

        switch (state) {
            case CLASSIC:
                openClassic();
                break;
            case GAMES:
                openGames();
                break;
            case FAVORITE:
                openFavorite();
                break;
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Meme clickedMeme = (Meme) parent.getItemAtPosition(position);
                clickedMemeId = clickedMeme.getId();
                if (clickedMeme.checkPurchase(purchasedPreferences)) {
                    // ... go to the meme fragment
                    MemeFragment memeFragment = new MemeFragment();
                    Bundle args = new Bundle();
                    args.putInt("id", clickedMemeId);
                    args.putString("title", clickedMeme.getTitle());
                    memeFragment.setArguments(args);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main, memeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    if (appPreferences.getInt(PREFERENCES_APP_COINS, 0) >= MEME_PRICE)
                        showPurchaseDialog();
                    else showDontEnoughCoinsDialog();
                }
            }
        });

        return rootView;
    }

    private void initializePreferences() {
        purchasedPreferences = getActivity().getSharedPreferences(PREFERENCES_PURCHASE, Context.MODE_PRIVATE);
        editor = purchasedPreferences.edit();
        for (Integer id = 1; id <= CLASSIC_COUNT; id++) {
            if (!purchasedPreferences.contains(id.toString())) {
                if (id > CLASSIC_OPEN_COUNT) editor.putBoolean(id.toString(), false);
                else editor.putBoolean(id.toString(), true);
            }
        }
        for (Integer id = 101; id <= GAMES_COUNT + 100; id++) {
            if (!purchasedPreferences.contains(id.toString())) {
                if (id > GAMES_OPEN_COUNT + 100) editor.putBoolean(id.toString(), false);
                else editor.putBoolean(id.toString(), true);
            }
        }
        editor.apply();

        favoritePreferences = getActivity().getSharedPreferences(PREFERENCES_FAVORITE, Context.MODE_PRIVATE);
        editor = favoritePreferences.edit();
        for (Integer id = 1; id <= CLASSIC_COUNT; id++) {
            if (!favoritePreferences.contains(id.toString()))
                editor.putBoolean(id.toString(), true);                                             //  FALSE   !!!
        }
        for (Integer id = 101; id <= GAMES_COUNT + 100; id++) {
            if (!favoritePreferences.contains(id.toString()))
                editor.putBoolean(id.toString(), false);
        }
        editor.apply();

        appPreferences = getActivity().getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE);
        if (!appPreferences.contains(PREFERENCES_APP_COINS)) {
            editor = appPreferences.edit();
            editor.putInt(PREFERENCES_APP_COINS, START_COINS);
            editor.apply();
        }
    }

    public void openClassic() {
        toolbarTitle.setText(getString(R.string.classic));
        if (classic == null) loadClassic();
        imageAdapter = new ImageAdapter(getActivity(), classic, purchasedPreferences);
        gridView.setAdapter(imageAdapter);
        gridView.refreshDrawableState();
    }

    public void openGames() {
        toolbarTitle.setText(getString(R.string.games));
        if (games == null) loadGames();
        imageAdapter = new ImageAdapter(getActivity(), games, purchasedPreferences);
        gridView.setAdapter(imageAdapter);
        gridView.refreshDrawableState();
    }

    public void openFavorite() {
        toolbarTitle.setText(getString(R.string.favorite));
        if (favorite == null) loadFavorite();
        imageAdapter = new ImageAdapter(getActivity(), favorite, purchasedPreferences);
        gridView.setAdapter(imageAdapter);
        gridView.refreshDrawableState();
    }

    private void loadClassic() {
        classic = new ArrayList<Meme>();
        classic.add(new Meme(1, "Геннадий Горин", R.drawable.classic_1_gorin));
        classic.add(new Meme(2, "Sample", R.drawable.lil));
        classic.add(new Meme(3, "Sample", R.drawable.lil));
        classic.add(new Meme(4, "Sample", R.drawable.lil));
        classic.add(new Meme(5, "Sample", R.drawable.kit));
        classic.add(new Meme(6, "Sample", R.drawable.kit));
        classic.add(new Meme(7, "Sample", R.drawable.kit));
        classic.add(new Meme(8, "Sample", R.drawable.kit));
        classic.add(new Meme(9, "Sample", R.drawable.classic_1_gorin));
        classic.add(new Meme(10, "Sample", R.drawable.lil));
        classic.add(new Meme(11, "Sample", R.drawable.lil));
        classic.add(new Meme(12, "Sample", R.drawable.lil));
        classic.add(new Meme(13, "Sample", R.drawable.kit));
        classic.add(new Meme(14, "Sample", R.drawable.kit));
        classic.add(new Meme(15, "Sample", R.drawable.kit));
        classic.add(new Meme(16, "Sample", R.drawable.kit));
    }

    private void loadGames() {
        games = new ArrayList<Meme>();
        games.add(new Meme(101, "Сова", R.drawable.classic_2_sova));
        games.add(new Meme(102, "Sample", R.drawable.kit));
        games.add(new Meme(103, "Sample", R.drawable.kit));
        games.add(new Meme(104, "Sample", R.drawable.kit));
        games.add(new Meme(105, "Sample", R.drawable.kit));
        games.add(new Meme(106, "Sample", R.drawable.kit));
        games.add(new Meme(107, "Sample", R.drawable.lil));
        games.add(new Meme(108, "Sample", R.drawable.lil));
        games.add(new Meme(109, "Sample", R.drawable.classic_2_sova));
        games.add(new Meme(110, "Sample", R.drawable.kit));
        games.add(new Meme(111, "Sample", R.drawable.kit));
        games.add(new Meme(112, "Sample", R.drawable.kit));
        games.add(new Meme(113, "Sample", R.drawable.kit));
        games.add(new Meme(114, "Sample", R.drawable.kit));
        games.add(new Meme(115, "Sample", R.drawable.lil));
        games.add(new Meme(116, "Sample", R.drawable.lil));
    }

    private void loadFavorite() {
        if (classic == null) loadClassic();
        if (games == null) loadGames();

        favorite = new ArrayList<Meme>();
        for (Integer id = 1; id <= CLASSIC_COUNT; id++) {
            if (favoritePreferences.getBoolean(id.toString(), false))
                favorite.add(classic.get(id - 1));
        }
        for (Integer id = 101; id <= GAMES_COUNT; id++) {
            if (favoritePreferences.getBoolean(id.toString(), false))
                favorite.add(games.get(id - 1));
        }
    }

    /**
     * Refresh coins statement on layout.
     *
     * @param coinsChanges If not null, changes coins count in preferences.
     */
    private void refreshCoins(int coinsChanges) {
        if (coinsChanges != 0) {
            editor = appPreferences.edit();
            editor.putInt(PREFERENCES_APP_COINS,
                    appPreferences.getInt(PREFERENCES_APP_COINS, 0) + coinsChanges);
            editor.apply();
        }

        toolbarCoins.setText(String.valueOf(appPreferences.getInt(PREFERENCES_APP_COINS, 0)));
    }

    private void showPurchaseDialog() {
        if (purchaseDialog == null) {
            AlertDialog.Builder purchaseBuilder = new AlertDialog.Builder(getActivity());

            purchaseBuilder.setTitle(R.string.dialog_purchase_title);

            purchaseBuilder.setPositiveButton(R.string.dialog_purchase_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor = purchasedPreferences.edit();
                    editor.putBoolean(clickedMemeId.toString(), true);
                    editor.apply();
                    refreshCoins(-MEME_PRICE);

                    gridView.invalidateViews();
                }
            });

            purchaseBuilder.setNegativeButton(R.string.dialog_purchase_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            purchaseDialog = purchaseBuilder.create();
        }

        purchaseDialog.show();
    }

    private void showDontEnoughCoinsDialog() {
        if (dontEnoughCoinsDialog == null) {
            AlertDialog.Builder dontEnoughCoinsBuilder = new AlertDialog.Builder(getActivity());

            dontEnoughCoinsBuilder.setTitle(R.string.dialog_dont_enough_coins_title);

            dontEnoughCoinsBuilder.setPositiveButton(R.string.dialog_dont_enough_coins_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User closed the dialog
                }
            });

            dontEnoughCoinsDialog = dontEnoughCoinsBuilder.create();
        }

        dontEnoughCoinsDialog.show();
    }

    public enum State {
        CLASSIC,
        GAMES,
        FAVORITE;
    }
}
