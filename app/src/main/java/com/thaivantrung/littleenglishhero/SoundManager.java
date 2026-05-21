package com.thaivantrung.littleenglishhero;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {

    // BUTTON CLICK
    public static void playClick(Context context) {

        playSound(context, R.raw.sound_button_click);
    }

    // ĐÚNG

    // SAI

    // WIN

    // LEVEL UP

    // HÀM PHÁT CHUNG
    private static void playSound(
            Context context,
            int soundRes
    ) {

        MediaPlayer mediaPlayer =
                MediaPlayer.create(context, soundRes);

        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
        });
    }
}

