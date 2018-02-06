package com.gamesbars.myfavoritememes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String PREFERENCES_APP = "app";
    public final static String PREFERENCES_APP_COINS = "coins";
    public final static String PREFERENCES_FAVORITE = "favorite";
    public final static String PREFERENCES_PURCHASE = "purchase";

    private final static Integer START_COINS = 1000;  //  start count of coins
    private final static Integer MEME_PRICE = 100;  //   price of one meme
    private final static Integer CLASSIC_COUNT = 16;
    private final static Integer CLASSIC_OPEN_COUNT = 2; // initially purchased classic memes
    private final static Integer GAMES_COUNT = 16;
    private final static Integer GAMES_OPEN_COUNT = 2;   // initially purchased games memes

    private ArrayList<Meme> classic;
    private ArrayList<Meme> games;
    private ArrayList<Meme> favorite;

    private GridView gridView;
    private ImageAdapter imageAdapter;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TextView toolbarCoins;

    private AlertDialog purchaseDialog;
    private Integer clickedMemeId;
    private AlertDialog dontEnoughCoinsDialog;

    private SharedPreferences appPreferences;
    private SharedPreferences purchasedPreferences;
    private SharedPreferences favoritePreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarCoins = (TextView) findViewById(R.id.toolbar_coins);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializePreferences();

        loadClassic();
        refreshCoins(0);
        imageAdapter = new ImageAdapter(this, classic, purchasedPreferences);
        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Meme clickedMeme = (Meme) parent.getItemAtPosition(position);
                clickedMemeId = clickedMeme.getId();
                if (clickedMeme.checkPurchase(purchasedPreferences)) {
                    // ... go to the meme activity
                } else {
                    if (appPreferences.getInt(PREFERENCES_APP_COINS, 0) >= MEME_PRICE)
                    showPurchaseDialog();
                    else showDontEnoughCoinsDialog();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_classic) {
            toolbarTitle.setText(getString(R.string.classic));
            if (classic == null) loadClassic();
            imageAdapter = new ImageAdapter(this, classic, purchasedPreferences);
            gridView.setAdapter(imageAdapter);
            gridView.refreshDrawableState();
        } else if (id == R.id.nav_games) {
            toolbarTitle.setText(getString(R.string.games));
            if (games == null) loadGames();
            imageAdapter = new ImageAdapter(this, games, purchasedPreferences);
            gridView.setAdapter(imageAdapter);
            gridView.refreshDrawableState();
        } else if (id == R.id.nav_favorite) {
            toolbarTitle.setText(getString(R.string.favorite));
            if (favorite == null) loadFavorite();
            imageAdapter = new ImageAdapter(this, favorite, purchasedPreferences);
            gridView.setAdapter(imageAdapter);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializePreferences() {
        purchasedPreferences = getSharedPreferences(PREFERENCES_PURCHASE, Context.MODE_PRIVATE);
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

        favoritePreferences = getSharedPreferences(PREFERENCES_FAVORITE, Context.MODE_PRIVATE);
        editor = favoritePreferences.edit();
        for (Integer id = 1; id <= CLASSIC_COUNT; id++) {
            if (!favoritePreferences.contains(id.toString()))
                editor.putBoolean(id.toString(), false);
        }
        for (Integer id = 101; id <= GAMES_COUNT + 100; id++) {
            if (!favoritePreferences.contains(id.toString()))
                editor.putBoolean(id.toString(), false);
        }
        editor.apply();

        appPreferences = getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE);
        if (!appPreferences.contains(PREFERENCES_APP_COINS)) {
            editor = appPreferences.edit();
            editor.putInt(PREFERENCES_APP_COINS, START_COINS);
            editor.apply();
        }
    }

    private void loadClassic() {
        classic = new ArrayList<Meme>();
        classic.add(new Meme(1, R.drawable.classic_1_gorin));
        classic.add(new Meme(2, R.drawable.lil));
        classic.add(new Meme(3, R.drawable.lil));
        classic.add(new Meme(4, R.drawable.lil));
        classic.add(new Meme(5, R.drawable.kit));
        classic.add(new Meme(6, R.drawable.kit));
        classic.add(new Meme(7, R.drawable.kit));
        classic.add(new Meme(8, R.drawable.kit));
        classic.add(new Meme(9, R.drawable.classic_1_gorin));
        classic.add(new Meme(10, R.drawable.lil));
        classic.add(new Meme(11, R.drawable.lil));
        classic.add(new Meme(12, R.drawable.lil));
        classic.add(new Meme(13, R.drawable.kit));
        classic.add(new Meme(14, R.drawable.kit));
        classic.add(new Meme(15, R.drawable.kit));
        classic.add(new Meme(16, R.drawable.kit));
    }

    private void loadGames() {
        games = new ArrayList<Meme>();
        games.add(new Meme(101, R.drawable.classic_2_sova));
        games.add(new Meme(102, R.drawable.kit));
        games.add(new Meme(103, R.drawable.kit));
        games.add(new Meme(104, R.drawable.kit));
        games.add(new Meme(105, R.drawable.kit));
        games.add(new Meme(106, R.drawable.kit));
        games.add(new Meme(107, R.drawable.lil));
        games.add(new Meme(108, R.drawable.lil));
        games.add(new Meme(109, R.drawable.classic_2_sova));
        games.add(new Meme(110, R.drawable.kit));
        games.add(new Meme(111, R.drawable.kit));
        games.add(new Meme(112, R.drawable.kit));
        games.add(new Meme(113, R.drawable.kit));
        games.add(new Meme(114, R.drawable.kit));
        games.add(new Meme(115, R.drawable.lil));
        games.add(new Meme(116, R.drawable.lil));
    }

    private void loadFavorite() {
        if (classic == null) loadClassic();
        if (games == null) loadGames();

        favorite = new ArrayList<Meme>();
        for (Integer id = 1; id <= CLASSIC_COUNT; id++) {
            if (favoritePreferences.getBoolean(id.toString(), false))
                favorite.add(classic.get(id + 1));
        }
        for (Integer id = 101; id <= GAMES_COUNT; id++) {
            if (favoritePreferences.getBoolean(id.toString(), false))
                favorite.add(games.get(id + 1));
        }
    }

    /**
     *  Refresh coins statement on layout.
     *
     *  @param coinsChanges If not null, changes coins count in preferences.
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
            AlertDialog.Builder purchaseBuilder = new AlertDialog.Builder(this);

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
            AlertDialog.Builder dontEnoughCoinsBuilder = new AlertDialog.Builder(this);

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
}

