package com.example.vivian.guessmusic.model;

/**
 * Created by Vivian on 2017/6/4.
 */

public class Song {
    private String mSongName;
    private String mSongFileName;
    private int mNameLength;

    public char[] getNameCharacters(){
        return mSongName.toCharArray();
    }

    public int getNameLength() {
        return mNameLength;
    }



    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String songFileName) {
        this.mSongFileName = songFileName;
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songName) {
        this.mSongName = songName;
        this.mNameLength=songName.length();
    }
}
