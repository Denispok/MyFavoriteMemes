package com.gamesbars.myfavoritememes;

import java.util.ArrayList;

public final class Phrases {

    public static ArrayList<Phrase> getPhrases(int memeId) {
        ArrayList<Phrase> phrases = new ArrayList<Phrase>();
        switch (memeId) {
            case 1:
                phrases.add(new Phrase("text", 123));
                phrases.add(new Phrase("text2", 1234));
                break;
            // ...
        }
        return phrases;
    }
}
