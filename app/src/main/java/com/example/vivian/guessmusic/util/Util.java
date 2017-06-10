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
import com.example.vivian.guessmusic.model.IAlertDialogButtonListener;

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
    public static void showDialog(Context context, String message,
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

            }
        });
        btnCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlterDialog!=null){
                    mAlterDialog.cancel();
                }
            }
        });
        //为dialog设置view
        builder.setView(dialogView);
        mAlterDialog=builder.create();
        //显示对话框
        mAlterDialog.show();
    }
}
