package com.gamesbars.myfavoritememes;

import com.gamesbars.myfavoritememes.Fragments.MemeFragment;

import java.util.Arrays;
import java.util.List;

public final class Memes {

    private static final Meme[] classic = new Meme[]{
            new Meme(1, "Геннадий Горин", R.drawable.classic_1_gorin),
            new Meme(2, "Sample", R.drawable.lil),
            new Meme(3, "Sample", R.drawable.lil),
            new Meme(4, "Sample", R.drawable.lil),
            new Meme(5, "Sample", R.drawable.kit),
            new Meme(6, "Sample", R.drawable.kit),
            new Meme(7, "Sample", R.drawable.kit),
            new Meme(8, "Sample", R.drawable.kit),
            new Meme(9, "Sample", R.drawable.classic_1_gorin),
            new Meme(10, "Sample", R.drawable.lil),
            new Meme(11, "Sample", R.drawable.lil),
            new Meme(12, "Sample", R.drawable.lil),
            new Meme(13, "Sample", R.drawable.kit),
            new Meme(14, "Sample", R.drawable.kit),
            new Meme(15, "Sample", R.drawable.kit),
            new Meme(16, "Sample", R.drawable.kit)};

    private static final Meme[] games = new Meme[]{
            new Meme(101, "Сова", R.drawable.classic_2_sova),
            new Meme(102, "Sample", R.drawable.kit),
            new Meme(103, "Sample", R.drawable.kit),
            new Meme(104, "Sample", R.drawable.kit),
            new Meme(105, "Sample", R.drawable.kit),
            new Meme(106, "Sample", R.drawable.kit),
            new Meme(107, "Sample", R.drawable.lil),
            new Meme(108, "Sample", R.drawable.lil),
            new Meme(109, "Sample", R.drawable.classic_2_sova),
            new Meme(110, "Sample", R.drawable.kit),
            new Meme(111, "Sample", R.drawable.kit),
            new Meme(112, "Sample", R.drawable.kit),
            new Meme(113, "Sample", R.drawable.kit),
            new Meme(114, "Sample", R.drawable.kit),
            new Meme(115, "Sample", R.drawable.lil),
            new Meme(116, "Sample", R.drawable.lil)};

    public static List<Meme> getMemes(MemeFragment.Category category) {
        switch (category) {
            case CLASSIC:
                return Arrays.asList(classic);

            case GAMES:
                return Arrays.asList(games);

            default:
                return null;
        }
    }

    public static Meme getMeme(int memeId) {
        if (memeId <= 100) return classic[memeId - 1];
        else return games[memeId - 101];
    }
}
