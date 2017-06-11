package com.example.vivian.guessmusic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vivian.guessmusic.R;
import com.example.vivian.guessmusic.util.MyPlayer;

/**
 * Created by banz on 2017/6/7.
 * 通关界面
 */

public class AppPassView extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pass_view);
        ImageButton btnBack= (ImageButton) findViewById(R.id.btn_bar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AppPassView.this,"挑战已结束",Toast.LENGTH_SHORT).show();
            }
        });

        //隐藏右上角的金币按钮
        FrameLayout view= (FrameLayout) findViewById(R.id.layout_bar_coin);
        view.setVisibility(View.INVISIBLE);
    }
}
