package com.gamesbars.myfavoritememes;


public class Phrase {

    private final int soundId;
    private final String text;

    public Phrase(String text, int soundId) {
        this.soundId = soundId;
        this.text = text;
    }

    public int getSoundId() {
        return soundId;
    }

    public String getText() {
        return text;
    }
}
