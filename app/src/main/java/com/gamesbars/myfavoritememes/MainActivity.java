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
import android.widget.TextView;

import com.gamesbars.myfavoritememes.Fragments.ListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String PREFERENCES_APP = "app";
    public final static String PREFERENCES_APP_COINS = "coins";
    public final static String PREFERENCES_FAVORITE = "favorite";
    public final static String PREFERENCES_PURCHASE = "purchase";

    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, new ListFragment())
                .commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
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

        ListFragment listFragment;

        //  Create new ListFragment or use old if one already exist
        if (fragmentManager.findFragmentById(R.id.content_main) instanceof ListFragment) {
            listFragment = (ListFragment) fragmentManager.findFragmentById(R.id.content_main);
        } else {
            listFragment = new ListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, listFragment)
                    .commit();
        }

        if (id == R.id.nav_classic) {
            toolbarTitle.setText(getString(R.string.classic));
            listFragment.openClassic();
            listFragment.state = ListFragment.State.CLASSIC;

        } else if (id == R.id.nav_games) {
            toolbarTitle.setText(getString(R.string.games));
            listFragment.openGames();
            listFragment.state = ListFragment.State.GAMES;

        } else if (id == R.id.nav_favorite) {
            toolbarTitle.setText(getString(R.string.favorite));
            listFragment.openFavorite();
            listFragment.state = ListFragment.State.FAVORITE;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

