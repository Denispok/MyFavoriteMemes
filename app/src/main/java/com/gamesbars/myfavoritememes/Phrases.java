package com.gamesbars.myfavoritememes;

import java.util.ArrayList;
import java.util.List;

public final class Phrases {

    public static List<Phrase> getPhrases(int memeId) {
        List<Phrase> phrases = new ArrayList<>();
        switch (memeId) {
            case 101:
                phrases.add(new Phrase("Мне потанцевать нельзя?", R.raw.classic_1_1_no_dance));
                phrases.add(new Phrase("Ты за меня придурка не держи", R.raw.classic_1_2_pridurka_ne_derzhi));
                phrases.add(new Phrase("Да как ты смеешь!", R.raw.classic_1_3_da_kak_ti_smeesh));
                phrases.add(new Phrase("Ты - пидор несчастный!", R.raw.classic_1_4_ti_pidor));
                phrases.add(new Phrase("Видеосос", R.raw.classic_1_5_videosos));
                phrases.add(new Phrase("Мне нужны доказательства", R.raw.classic_1_6_dokazatelstva));
                phrases.add(new Phrase("Ты получишь дуэль!", R.raw.classic_1_7_ti_poluchish_duel));
                phrases.add(new Phrase("Я в ярости", R.raw.classic_1_8_ya_v_yarosti));
                phrases.add(new Phrase("Я тебя из-под земли достану", R.raw.classic_1_9_ya_tebya_dostanu));
                phrases.add(new Phrase("Тебе весело, а мне нет!", R.raw.classic_1_10_tebe_veselo));
                break;
            case 102:
                phrases.add(new Phrase("Ммм...", R.raw.classic_2_1_mmm));
                phrases.add(new Phrase("Повар спрашивает повара", R.raw.classic_2_2_cook_ask));
                phrases.add(new Phrase("Ты миллиционер?", R.raw.classic_2_3_you_policemen));
                phrases.add(new Phrase("Нет, - отвечает повар", R.raw.classic_2_4_cook_answer_no));
                phrases.add(new Phrase("Моя главная профессия...", R.raw.classic_2_5_my_main_profession));
                phrases.add(new Phrase("Пооооовар", R.raw.classic_2_6_cooook));
                phrases.add(new Phrase("А твоя наверно врач, повар??", R.raw.classic_2_7_your_prof_doctor));
                break;
            // ...
        }
        return phrases;
    }
}
