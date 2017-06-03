package com.example.vivian.guessmusic.ui;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vivian.guessmusic.R;
import com.example.vivian.guessmusic.model.IWordButtonClickListener;
import com.example.vivian.guessmusic.model.WordButton;
import com.example.vivian.guessmusic.myui.MyGridView;
import com.example.vivian.guessmusic.util.Util;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IWordButtonClickListener{
    public final static int COUNTS_WORDS=24;
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;

    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    private ImageView mViewPan;
    private ImageView mViewPanBar;

    //Play 按键事件
    private ImageButton mBtnPlayStart;

    private boolean mIsRunning=false;
    //文字框容器
    private ArrayList<WordButton> mAllWords;
    private ArrayList<WordButton> mBtnSelsecWords;
    private MyGridView mMyGridView;
    //已选择文字框UI容器
    private LinearLayout mViewWordsContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewWordsContainer= (LinearLayout) findViewById(R.id.word_select_container);

        mViewPan= (ImageView) findViewById(R.id.imageView1);
        mViewPanBar= (ImageView) findViewById(R.id.imageView2);
        mMyGridView= (MyGridView) findViewById(R.id.gridview);
        mMyGridView.registOnWordButtonClick(this);
        //初始化动画
        mPanAnim= AnimationUtils.loadAnimation(this,R.anim.rotate);
        mPanLin=new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPanBar.startAnimation(mBarOutAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarInAnim= AnimationUtils.loadAnimation(this,R.anim.rotate_45);
        mBarInLin=new LinearInterpolator();
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBarOutAnim= AnimationUtils.loadAnimation(this,R.anim.rotate_d_45);
        mBarOutLin=new LinearInterpolator();
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mIsRunning=false;
                mBtnPlayStart.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnPlayStart= (ImageButton) findViewById(R.id.btn_play_start);
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"Hello",Toast.LENGTH_SHORT).show();
                handlePlayButton();
            }
        });
        //初始化游戏数据
        initCurrentStageData();
    }
    private  void handlePlayButton(){
        if (mViewPanBar!=null) {
            if (!mIsRunning) {
                mIsRunning = true;
                mViewPanBar.startAnimation(mBarInAnim);
                mBtnPlayStart.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
        mViewPan.clearAnimation();
        super.onPause();
    }
    public void initCurrentStageData(){
        //初始化已选择框
        mBtnSelsecWords=initWordSelect();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(140,140);
        for (int i=0;i<mBtnSelsecWords.size();i++){
            mViewWordsContainer.addView(
                    mBtnSelsecWords.get(i).mViewButton,
                    params);

        }

        //获得数据
        mAllWords=initAllword();


        //更新数据 MyGridView
        mMyGridView.updateData(mAllWords);
    }

    /**
     * 初始化待选文字框
     * @return
     */
    private ArrayList<WordButton> initAllword(){
        ArrayList<WordButton> data=new ArrayList<>();
        //获得所有待选文字
        //.......
        for(int i=0;i<COUNTS_WORDS;i++){
            WordButton button=new WordButton();
            button.mWordString="good";
            data.add(button);

        }
        return data;
    }
    /**
     * 初始化已选择文字框
     */
    private ArrayList<WordButton> initWordSelect(){
        ArrayList<WordButton> data=new ArrayList<>();

        for (int i=0;i<4;i++){
            View view= Util.getView(MainActivity.this,
                    R.layout.self_ui_gridview_item);
            WordButton holder=new WordButton();
            holder.mViewButton = (Button)view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable=false;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            data.add(holder);

        }
        return data;
    }

    @Override
    public void onWordButtonClick(WordButton wordButton) {

    }
}
