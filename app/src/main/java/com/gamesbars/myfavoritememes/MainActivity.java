package com.gamesbars.myfavoritememes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gamesbars.myfavoritememes.Fragments.MemeFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String PREFERENCES_APP = "app";
    public final static String PREFERENCES_APP_COINS = "coins";
    public final static String PREFERENCES_FAVORITE = "favorite";
    public final static String PREFERENCES_PURCHASE = "purchase";

    public final static Integer MEME_PRICE = 100;  //   price of one meme
    private final static Integer START_COINS = 1000;  //  start count of coins
    private final static List<Integer> MODERN_OPEN = Arrays.asList(1); // initially purchased modern memes
    private final static List<Integer> CLASSIC_OPEN = Arrays.asList(101, 102, 103, 104); // initially purchased classic memes
    private final static List<Integer> GAMES_OPEN = Arrays.asList(201, 202, 203, 204);   // initially purchased games memes

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePreferences();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, new MemeFragment())
                .commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() == 1) {
            fragmentManager.popBackStackImmediate();
        }

        //if (id == R.id.setting , etc...)

        MemeFragment memeFragment;

        //  Create new MemeFragment or use old if one already exist
        if (fragmentManager.findFragmentById(R.id.content_main) instanceof MemeFragment) {
            memeFragment = (MemeFragment) fragmentManager.findFragmentById(R.id.content_main);
        } else {
            memeFragment = new MemeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, memeFragment)
                    .commit();
        }

        if (id == R.id.nav_modern) memeFragment.openModern();
        if (id == R.id.nav_classic) memeFragment.openClassic();
        else if (id == R.id.nav_games) memeFragment.openGames();
        else if (id == R.id.nav_favorite) memeFragment.openFavorite();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializePreferences() {
        SharedPreferences purchasedPreferences = getSharedPreferences(PREFERENCES_PURCHASE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = purchasedPreferences.edit();
        for (Meme meme : Memes.getMemes(MemeFragment.Category.MODERN)) {
            Integer memeId = meme.getId();
            if (!purchasedPreferences.contains(memeId.toString())) {
                if (MODERN_OPEN.contains(memeId)) editor.putBoolean(memeId.toString(), true);
                else editor.putBoolean(memeId.toString(), false);
            }
        }
        for (Meme meme : Memes.getMemes(MemeFragment.Category.CLASSIC)) {
            Integer memeId = meme.getId();
            if (!purchasedPreferences.contains(memeId.toString())) {
                if (CLASSIC_OPEN.contains(memeId)) editor.putBoolean(memeId.toString(), true);
                else editor.putBoolean(memeId.toString(), false);
            }
        }
        for (Meme meme : Memes.getMemes(MemeFragment.Category.GAMES)) {
            Integer memeId = meme.getId();
            if (!purchasedPreferences.contains(memeId.toString())) {
                if (GAMES_OPEN.contains(memeId)) editor.putBoolean(memeId.toString(), true);
                else editor.putBoolean(memeId.toString(), false);
            }
        }
        editor.apply();

        SharedPreferences appPreferences = getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE);
        if (!appPreferences.contains(PREFERENCES_APP_COINS)) {
            editor = appPreferences.edit();
            editor.putInt(PREFERENCES_APP_COINS, START_COINS);
            editor.apply();
        }
    }
}

