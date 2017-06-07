package com.example.vivian.guessmusic.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Vivian on 2017/5/31.
 */

public class Util {
    public static View getView(Context context, int layoutId){
        LayoutInflater inflater= (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=inflater.inflate(layoutId,null);
        return layout;
    }

    /**
     * 界面跳转
     * @param context
     * @param desti
     */
    public static void startActivity(Context context,Class desti){
        Intent intent=new Intent();
        intent.setClass(context,desti);
        context.startActivity(intent);
        //关闭当前的Activity
        ((Activity)context).finish();
    }
}
