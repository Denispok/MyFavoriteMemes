package com.gamesbars.myfavoritememes;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String PREFERENCES_APP = "app";
    public final static String PREFERENCES_APP_COINS = "coins";
    public final static String PREFERENCES_FAVORITE = "favorite";
    public final static String PREFERENCES_PURCHASE = "purchase";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (id == R.id.nav_classic) memeFragment.openClassic();
        else if (id == R.id.nav_games) memeFragment.openGames();
        else if (id == R.id.nav_favorite) memeFragment.openFavorite();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

