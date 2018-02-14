package com.majhiwala.maijied.catchthebird;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by Maijied on 12/02/17.
 */

public class soundPlayer {
    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX =2;
    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;

    public soundPlayer(Context context){

        //SoundPool is deprecated in API level 21(Lolipop)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();


            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        } else{
            //SoundPool (int maxStreams, int stramType, int srcQuality)
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }


        hitSound = soundPool.load(context, R.raw.hit,1);
        overSound = soundPool.load(context, R.raw.over,1);
    }
    public void playHitSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playOverSound(){
        soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
