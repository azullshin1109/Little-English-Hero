package com.thaivantrung.littleenglishhero;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;

    // phát nhạc
    public static void playMusic(Context context) {

        if (mediaPlayer == null) {

            mediaPlayer = MediaPlayer.create(context, R.raw.intro_step);

            mediaPlayer.setLooping(true);

            mediaPlayer.setVolume(0.4f, 0.4f);

            mediaPlayer.start();
        }
    }

    // tạm dừng
    public static void pauseMusic() {

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    // phát tiếp
    public static void resumeMusic() {

        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    // tắt hẳn
    public static void stopMusic() {

        if (mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

