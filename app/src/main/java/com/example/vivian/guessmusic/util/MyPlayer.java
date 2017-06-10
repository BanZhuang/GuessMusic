package com.example.vivian.guessmusic.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 音乐播放类
 * Created by banz on 2017/6/8.
 */

public class MyPlayer {
    public final static int INDEX_STONE_ENTER=0;
    public final static int INDEX_STONE_CANCEL=1;
    public final static int INDEX_STONE_COIN=2;
    private final static String[] SONG_NAMES=
            {"enter.mp3","cancel.mp3","coin.mp3"};
    //音效
    private static MediaPlayer[] mToneMediaPlayer=new MediaPlayer[SONG_NAMES.length];
    //歌曲播放
    private static MediaPlayer mMusicMediaPlayer;

    /**
     * 播放歌曲
     * @param context
     * @param fileName
     */
    public static void playSong(Context context,String fileName){
        if (mMusicMediaPlayer==null){
            mMusicMediaPlayer=new MediaPlayer();
        }
        //强制重置
        mMusicMediaPlayer.reset();

        //加载声音文件
        AssetManager assetManager=context.getAssets();
        try {
            AssetFileDescriptor fileDescriptor=assetManager.openFd(fileName);
            mMusicMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor()
                    ,fileDescriptor.getStartOffset()
                    ,fileDescriptor.getLength());
            mMusicMediaPlayer.prepare();
            mMusicMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void stopTheSong(Context context){
        if (mMusicMediaPlayer!=null){
            mMusicMediaPlayer.stop();
        }
    }
}