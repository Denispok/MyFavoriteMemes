package com.gamesbars.myfavoritememes;

import java.util.ArrayList;
import java.util.List;

public final class Phrases {

    public static List<Phrase> getPhrases(int memeId) {
        List<Phrase> phrases = new ArrayList<>();
        switch (memeId) {
            case 1:
                phrases.add(new Phrase("А я не понел", R.raw.classic_1_ne_ponel));
                break;
            case 2:
                phrases.add(new Phrase("Ааа... а я думала сова", R.raw.classic_2_aaa_sova));
                break;
            // ...
        }
        return phrases;
    }
}
