package com.thaivantrung.littleenglishhero;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {

    private static boolean isEffectOn = true;

    public static void setEffectOn(boolean on) {
        isEffectOn = on;
    }

    // BUTTON CLICK
    public static void playClick(Context context) {
        if (!isEffectOn) return;
        playSound(context, R.raw.sound_button_click);
    }

    // ÂM THANH TRẢ LỜI ĐÚNG
    public static void playCorrect(Context context) {
        if (!isEffectOn) return;
        playSound(context, R.raw.sound_victory);
    }

    // ÂM THANH TRẢ LỜI SAI
    public static void playWrong(Context context) {
        if (!isEffectOn) return;
        playSound(context, R.raw.sound_wrong);
    }

    private static void playSound(Context context, int soundRes) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, soundRes);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
        });
    }
}