package com.gamesbars.myfavoritememes;


import android.content.SharedPreferences;

public class Meme {

    private Integer id;
    private String title;
    private int imageResourceId;

    public Meme(Integer id, String title, int imageResourceId) {
        this.id = id;
        this.title = title;
        this.imageResourceId = imageResourceId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public boolean isPurchased(SharedPreferences purchasedMemes) {
        return purchasedMemes.getBoolean(id.toString(), false);
    }

    public boolean isFavorite(SharedPreferences favoriteMemes) {
        return favoriteMemes.getBoolean(id.toString(), false);
    }
}

