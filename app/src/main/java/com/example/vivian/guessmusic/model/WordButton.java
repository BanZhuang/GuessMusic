package com.example.vivian.guessmusic.model;

import android.widget.Button;

/**
 * Created by Vivian on 2017/5/31.
 */

public class WordButton {
    public int mIndex;
    public boolean mIsVisiable;
    public String mWordString;

    public Button mViewButton;

    public WordButton(){
        mIsVisiable=true;
        mWordString="";
    }
}
