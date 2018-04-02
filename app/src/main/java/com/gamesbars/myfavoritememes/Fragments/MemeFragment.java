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

import com.gamesbars.myfavoritememes.Meme;
import com.gamesbars.myfavoritememes.MemeAdapter;
import com.gamesbars.myfavoritememes.Memes;
import com.gamesbars.myfavoritememes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.gamesbars.myfavoritememes.MainActivity.MEME_PRICE;
import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_APP;
import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_APP_COINS;
import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_FAVORITE;
import static com.gamesbars.myfavoritememes.MainActivity.PREFERENCES_PURCHASE;

public class MemeFragment extends Fragment {

    private Category category;

    private GridView gridView;
    private MemeAdapter memeAdapter;

    private TextView toolbarTitle;
    private TextView toolbarCoins;

    private AlertDialog purchaseDialog;
    private Integer clickedMemeId;
    private AlertDialog dontEnoughCoinsDialog;

    private SharedPreferences appPreferences;
    private SharedPreferences purchasedPreferences;
    private SharedPreferences favoritePreferences;
    private SharedPreferences.Editor editor;

    public MemeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = Category.CLASSIC;

        purchasedPreferences = getActivity().getSharedPreferences(PREFERENCES_PURCHASE, Context.MODE_PRIVATE);
        favoritePreferences = getActivity().getSharedPreferences(PREFERENCES_FAVORITE, Context.MODE_PRIVATE);
        appPreferences = getActivity().getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE);
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

        openCurrentCategory();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Meme clickedMeme = (Meme) parent.getItemAtPosition(position);
                clickedMemeId = clickedMeme.getId();
                if (clickedMeme.isPurchased(purchasedPreferences)) {
                    // ... go to the phrase fragment
                    PhraseFragment phraseFragment = new PhraseFragment();
                    Bundle args = new Bundle();
                    args.putInt("id", clickedMemeId);
                    args.putString("title", clickedMeme.getTitle());
                    phraseFragment.setArguments(args);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main, phraseFragment);
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

    private void openCurrentCategory() {
        switch (category) {
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
    }

    public void openClassic() {
        toolbarTitle.setText(getString(R.string.classic));
        memeAdapter = new MemeAdapter(getActivity(), loadCategory(Category.CLASSIC), purchasedPreferences);
        gridView.setAdapter(memeAdapter);
        gridView.refreshDrawableState();
        category = Category.CLASSIC;
    }

    public void openGames() {
        toolbarTitle.setText(getString(R.string.games));
        memeAdapter = new MemeAdapter(getActivity(), loadCategory(Category.GAMES), purchasedPreferences);
        gridView.setAdapter(memeAdapter);
        gridView.refreshDrawableState();
        category = Category.GAMES;
    }

    public void openFavorite() {
        toolbarTitle.setText(getString(R.string.favorite));
        memeAdapter = new MemeAdapter(getActivity(), loadFavorite(), purchasedPreferences);
        gridView.setAdapter(memeAdapter);
        gridView.refreshDrawableState();
        category = Category.FAVORITE;
    }

    private List<Meme> loadCategory(Category category) {
        List<Meme> memes = Memes.getMemes(category);
        List<Meme> sortedMemes = new ArrayList<>();
        List<Meme> nonPurchasedMemes = new ArrayList<>();

        for (Meme meme : memes) {
            if (meme.isPurchased(purchasedPreferences)) sortedMemes.add(meme);
            else nonPurchasedMemes.add(meme);
        }

        Collections.reverse(sortedMemes);
        Collections.reverse(nonPurchasedMemes);
        sortedMemes.addAll(nonPurchasedMemes);
        return sortedMemes;
    }

    private List<Meme> loadFavorite() {
        List<Meme> favorite = new ArrayList<>();

        Set<String> favoriteKeys = favoritePreferences.getAll().keySet();
        List<Integer> favoriteIds = new ArrayList<>();

        for (String id : favoriteKeys) {
            favoriteIds.add(Integer.parseInt(id));
        }

        Collections.sort(favoriteIds, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 >= 1 && o1 <= 100 && o2 >= 101 && o2 <= 200) return -1;
                if (o2 >= 1 && o2 <= 100 && o1 >= 101 && o1 <= 200) return 1;
                if (o1 > o2) return -1;
                if (o1 < o2) return 1;
                return 0;
            }
        });

        for (Integer id : favoriteIds) {
            favorite.add(Memes.getMeme(id));
        }

        return favorite;
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

                    openCurrentCategory();
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

    public enum Category {
        CLASSIC,
        GAMES,
        FAVORITE
    }
}
