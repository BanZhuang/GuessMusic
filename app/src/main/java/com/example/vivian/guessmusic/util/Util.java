package com.example.vivian.guessmusic.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vivian.guessmusic.R;
import com.example.vivian.guessmusic.data.Const;
import com.example.vivian.guessmusic.model.IAlertDialogButtonListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Vivian on 2017/5/31.
 */

public class Util {
    public static AlertDialog mAlterDialog;
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

    /**
     * 显示自定义对话框
     * @param context
     * @param message
     * @param listener
     */
    public static void showDialog(final Context context, String message,
                                  final IAlertDialogButtonListener listener){
        View dialogView=null;

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        dialogView=getView(context, R.layout.dialog_view);

        ImageButton btnOkView= (ImageButton) dialogView.findViewById(R.id.btn_dialog_ok);
        ImageButton btnCancelView= (ImageButton) dialogView.findViewById(R.id.btn_dialog_cancel);
        TextView txtMessageView= (TextView) dialogView.findViewById(R.id.text_dialog_message);
        txtMessageView.setText(message);
        btnOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭对话框
                if (mAlterDialog!=null){
                    mAlterDialog.cancel();
                }
                //事件回调
                if (listener!=null){
                    listener.onClick();
                }
                //播放音效
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_ENTER);

            }
        });
        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlterDialog!=null){
                    mAlterDialog.cancel();
                }
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_CANCEL);
            }
        });
        //为dialog设置view
        builder.setView(dialogView);
        mAlterDialog=builder.create();
        //显示对话框
        mAlterDialog.show();
    }

    /**
     * 游戏数据保存
     * @param context
     * @param stageIndex
     * @param coins
     */
    public static void saveData(Context context,int stageIndex,int coins){
        FileOutputStream fileOutputStream=null;

        try {
            fileOutputStream=context.openFileOutput(Const.FILE_NAME_SAVE_DATA,
                    Context.MODE_PRIVATE);
            DataOutputStream dos=new DataOutputStream(fileOutputStream);

            dos.writeInt(stageIndex);
            dos.writeInt(coins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取游戏数据
     * @param context
     * @return
     */
    public static int[] loadData(Context context){
        FileInputStream fileInputStream=null;
        int[] datas={-1,Const.TOTAL_COINS};
        try {
            fileInputStream=context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dis=new DataInputStream(fileInputStream);
            datas[Const.INDEX_LOAD_DATA_STAGE]=dis.readInt();
            datas[Const.INDEX_LOAD_DATA_COINS]=dis.readInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
