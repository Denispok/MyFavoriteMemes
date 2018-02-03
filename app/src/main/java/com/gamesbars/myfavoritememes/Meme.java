package com.gamesbars.myfavoritememes;


import android.content.SharedPreferences;

public class Meme {

    private Integer id;
    private int imageResourceId;

    public Meme(Integer id, int imageResourceId) {
        this.id = id;
        this.imageResourceId = imageResourceId;
    }

    public Integer getId() {
        return id;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public boolean checkPurchase(SharedPreferences purchasedMemes) {
        return purchasedMemes.getBoolean(id.toString(), false);
    }

    public boolean checkFavorite(SharedPreferences favoriteMemes) {
        return favoriteMemes.getBoolean(id.toString(), false);
    }
}
