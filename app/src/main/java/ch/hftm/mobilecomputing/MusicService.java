package ch.hftm.mobilecomputing;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

    public static final String ACTION_START = "action_start";
    public static final String EXTRA_SONG = "extra_song";

    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent != null && ACTION_START.equals(intent.getAction())) {
            this.startMusic(intent.getIntExtra(EXTRA_SONG, R.raw.over_and_over));
        }

        return START_STICKY;
    }

    private void startMusic(int song) {
        this.mediaPlayer = MediaPlayer.create(this, song);
        this.mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mediaPlayer.pause();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}