package com.gamesbars.myfavoritememes;

import com.gamesbars.myfavoritememes.Fragments.MemeFragment;

import java.util.Arrays;
import java.util.List;

public final class Memes {

    private static final Meme[] modern = new Meme[]{
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

    private static final Meme[] classic = new Meme[]{
            new Meme(101, "Гамаз", R.drawable.classic_1_gamaz),
            new Meme(102, "Повар", R.drawable.classic_2_povar)};

    private static final Meme[] games = new Meme[]{
            new Meme(201, "Сова", R.drawable.classic_2_sova),
            new Meme(202, "Sample", R.drawable.kit),
            new Meme(203, "Sample", R.drawable.kit),
            new Meme(204, "Sample", R.drawable.kit),
            new Meme(205, "Sample", R.drawable.kit),
            new Meme(206, "Sample", R.drawable.kit),
            new Meme(207, "Sample", R.drawable.lil),
            new Meme(208, "Sample", R.drawable.lil),
            new Meme(209, "Sample", R.drawable.classic_2_sova),
            new Meme(210, "Sample", R.drawable.kit),
            new Meme(211, "Sample", R.drawable.kit),
            new Meme(212, "Sample", R.drawable.kit),
            new Meme(213, "Sample", R.drawable.kit),
            new Meme(214, "Sample", R.drawable.kit),
            new Meme(215, "Sample", R.drawable.lil),
            new Meme(216, "Sample", R.drawable.lil)};

    public static List<Meme> getMemes(MemeFragment.Category category) {
        switch (category) {
            case MODERN:
                return Arrays.asList(modern);

            case CLASSIC:
                return Arrays.asList(classic);

            case GAMES:
                return Arrays.asList(games);

            default:
                return null;
        }
    }

    public static Meme getMeme(int memeId) {
        if (memeId <= 100) return modern[memeId - 1];
        if (memeId <= 200) return classic[memeId - 101];
        return games[memeId - 201];
    }
}
