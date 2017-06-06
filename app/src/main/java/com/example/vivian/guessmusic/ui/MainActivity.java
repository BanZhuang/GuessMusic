package com.example.vivian.guessmusic.ui;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vivian.guessmusic.R;
import com.example.vivian.guessmusic.data.Const;
import com.example.vivian.guessmusic.model.IWordButtonClickListener;
import com.example.vivian.guessmusic.model.Song;
import com.example.vivian.guessmusic.model.WordButton;
import com.example.vivian.guessmusic.myui.MyGridView;
import com.example.vivian.guessmusic.util.MyLog;
import com.example.vivian.guessmusic.util.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements IWordButtonClickListener{
    public final static int COUNTS_WORDS=24;
    private static final String TAG = "MainActivity";
    /**
     * 答案状态
     */
    public final static int STATUS_ANSWER_RIGHT=1;
    public final static int STATUS_ANSWER_WRONG=2;
    public final static int STATUS_ANSWER_LACK=3;

    //闪烁次数
    public static final int SPASH_TIMES=6;
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
    //过关界面
    private View mPassView;

    private boolean mIsRunning=false;
    //文字框容器
    private ArrayList<WordButton> mAllWords;
    private ArrayList<WordButton> mBtnSelsecWords;
    private MyGridView mMyGridView;
    //已选择文字框UI容器
    private LinearLayout mViewWordsContainer;
    //当前的歌曲
    private Song mCurrentSong;
    //当前关的索引
    private int mCurrentStageIndex=-1;

    //当前金币数量
    private int mCurrentCoins=Const.TOTAL_COINS;

    //金币View
    private TextView mViewCurrentCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewWordsContainer= (LinearLayout) findViewById(R.id.word_select_container);

        mViewCurrentCoins= (TextView) findViewById(R.id.txt_bar_coins);
        mViewCurrentCoins.setText(mCurrentCoins+"");
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
        //处理删除按键事件
        handleDeleteWord();
        //处理提示按键事件
        handleTipAnswer();
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
    private Song loadStageSongInfo(int stageIndex){
        Song song=new Song();
        String[] stage= Const.SONG_INFO[stageIndex];
        song.setSongFileName(stage[Const.INDEX_FILE_NAME]);
        song.setSongName(stage[Const.INDEX_SONG_NAME]);
        return song;
    }
    public void initCurrentStageData(){
        //读取当前关的歌曲信息
        mCurrentSong=loadStageSongInfo(++mCurrentStageIndex);
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
        String[] words=generateWords();
        for(int i=0;i<COUNTS_WORDS;i++){
            WordButton button=new WordButton();
            button.mWordString=words[i];
            data.add(button);

        }
        return data;
    }
    /**
     * 初始化已选择文字框
     */
    private ArrayList<WordButton> initWordSelect(){
        ArrayList<WordButton> data=new ArrayList<>();

        for (int i=0;i<mCurrentSong.getNameLength();i++){
            View view= Util.getView(MainActivity.this,
                    R.layout.self_ui_gridview_item);
            final WordButton holder=new WordButton();
            holder.mViewButton = (Button)view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable=false;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            holder.mViewButton.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearTheAnswer(holder);
                }
            }));
            data.add(holder);

        }
        return data;
    }

    /**
     * 生成所有的待选文字
     * @return
     */
    private String[] generateWords(){
        Random random=new Random();
        String[] words=new String[MyGridView.COUNTS_WORDS];
        //存入歌名
        for(int i=0;i<mCurrentSong.getNameLength();i++){
            words[i]=mCurrentSong.getNameCharacters()[i]+"";

        }
        //获取随机文字
        for (int i=mCurrentSong.getNameLength();i<MyGridView.COUNTS_WORDS;i++){
            words[i]=getRandomChar()+"";
        }
        //打乱顺序:首先从所有元素中随机选取一个与第一个元素交换
        //然后在第二个之后选择一个元素与第二个交换，直到最后一个元素
        for(int i=MyGridView.COUNTS_WORDS-1;i>=0;i--){
            int index=random.nextInt(i+1);
            String buf=words[index];
            words[index]=words[i];
            words[i]=buf;
        }

        return words;

    }

    /**
     * 生成随机文字
     * @return
     */
    private char getRandomChar(){
        String str="";
        int highPos;
        int lowPos;

        Random random=new Random();
        highPos=(176+Math.abs(random.nextInt(39)));
        lowPos=(161+Math.abs(random.nextInt(93)));

        byte[] b=new byte[2];
        b[0]=(Integer.valueOf(highPos)).byteValue();
        b[1]=(Integer.valueOf(lowPos)).byteValue();

        try {
            str=new String(b,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }
    private void clearTheAnswer(WordButton wordButton){
        wordButton.mViewButton.setText("");
        wordButton.mWordString="";
        wordButton.mIsVisiable=false;
        //设置待选框的可见性
        setButtonVisiable(mAllWords.get(wordButton.mIndex),View.VISIBLE);
    }

    /**
     * 设置答案
     * @param wordButton
     */
    private void setSelectWord(WordButton wordButton){
        for(int i=0;i<mBtnSelsecWords.size();i++){
            if(mBtnSelsecWords.get(i).mWordString.length()==0){
                //设置答案文字框内容及可见性
                mBtnSelsecWords.get(i).mViewButton.setText(wordButton.mWordString);
                mBtnSelsecWords.get(i).mIsVisiable=true;
                mBtnSelsecWords.get(i).mWordString=wordButton.mWordString;
                //记录索引
                mBtnSelsecWords.get(i).mIndex=wordButton.mIndex;
                //Log
                MyLog.d(TAG,mBtnSelsecWords.get(i).mIndex+"");

                //设置待选框可见性
                setButtonVisiable(wordButton,View.INVISIBLE);
                break;

            }
        }
    }

    /**
     * 设置待选文字框是否可见
     * @param button
     * @param visibility
     */
    private void setButtonVisiable(WordButton button,int visibility){
        button.mViewButton.setVisibility(visibility);
        button.mIsVisiable=(visibility==View.VISIBLE)?true:false;

        //Log
        MyLog.d(TAG,button.mIsVisiable+"");

    }


    @Override
    public void onWordButtonClick(WordButton wordButton) {
        setSelectWord(wordButton);
        //获得答案状态
        int checkResult = checkTheAnswer();

        //检查答案
//        switch (checkResult){
//            case STATUS_ANSWER_RIGHT:
//                //过关并获得奖励
//                handlePassEvent();
//                break;
//            case STATUS_ANSWER_WRONG:
//                //错误提示
//                sparkTheWords();
//
//                break;
//            case STATUS_ANSWER_LACK:
//                //设置文字颜色为白色
//                for (int i=0;i<mBtnSelsecWords.size();i++){
//                    mBtnSelsecWords.get(i).mViewButton.setTextColor(Color.WHITE);
//                }
//                break;
//            default:break;
//        }
        if (checkResult == STATUS_ANSWER_RIGHT) {
            // 过关并获得奖励
//			Toast.makeText(this, "STATUS_ANSWER_RIGHT", Toast.LENGTH_SHORT).show();
            handlePassEvent();
        } else if (checkResult == STATUS_ANSWER_WRONG) {
            // 闪烁文字并提示用户
            sparkTheWords();
        } else if (checkResult == STATUS_ANSWER_LACK) {
            // 设置文字颜色为白色（Normal）
            for (int i = 0; i < mBtnSelsecWords.size(); i++) {
                mBtnSelsecWords.get(i).mViewButton.setTextColor(Color.WHITE);


            }
        }
    }

    /**
     * 处理过关界面及事件
     */
    private void handlePassEvent(){
        mPassView=this.findViewById(R.id.pass_view);
        mPassView.setVisibility(View.VISIBLE);

    }

    /**
     * 检查答案
     */
    private int checkTheAnswer(){
        //先检查长度
        for (int i=0;i<mBtnSelsecWords.size();i++){
            //如果有空的，说明答案不完整
            if (mBtnSelsecWords.get(i).mWordString.length()==0){
                return STATUS_ANSWER_LACK;
            }
        }
        //答案完整，继续检查正确性
        StringBuffer sb=new StringBuffer();
        for (int i=0;i<mBtnSelsecWords.size();i++){
            sb.append(mBtnSelsecWords.get(i).mWordString);
        }
        return (sb.toString().equals(mCurrentSong.getSongName()))?
                STATUS_ANSWER_RIGHT:STATUS_ANSWER_WRONG;
    }

    /**
     * 答案错误，闪烁文字
     */
    private void sparkTheWords(){
        //定时器相关
        TimerTask task=new TimerTask() {
            boolean mChange=false;
            int mSparkTimes=0;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (++mSparkTimes>SPASH_TIMES){
                            return;
                        }
                        //执行闪烁逻辑
                        for (int i=0;i<mBtnSelsecWords.size();i++){
                            mBtnSelsecWords.get(i).mViewButton.setTextColor(
                                    mChange?Color.RED:Color.WHITE
                            );
                        }
                        mChange=!mChange;
                    }
                });
            }
        };
        Timer timer=new Timer();
        timer.schedule(task,1,150);
    }
    /**
     * 自动选择一个答案
     */
    private void tipAnswer(){
        boolean tipWord = false;
        for (int i = 0; i < mBtnSelsecWords.size(); i++) {
            if (mBtnSelsecWords.get(i).mWordString.length() == 0) {
                // 根据当前的答案框条件选择对应的文字并填入
                onWordButtonClick(findAnswerWord(i));

                tipWord = true;

                // 减少金币数量
                if (!handleCoins(-getTipCoins())) {
                    // 金币数量不够，显示对话框
                    return;
                }
                break;
            }
        }

        // 没有找到可以填充的答案
        if (!tipWord) {
            // 闪烁文字提示用户
            sparkTheWords();
        }


    }
    /**
     * 删除文字
     */
    private void deleteOneWord(){
        //减少金币
        if (!handleCoins(-getDeleteWord())){
            //金币不够，显示提示对话框
            return;
        }
        //将这个索引对应的WordButton设置为不可见
        setButtonVisiable(findNotAnswerWord(),View.INVISIBLE);
    }
    /**
     * 随机找到一个不是答案的文字,并且当前是可见的
     */
    private WordButton findNotAnswerWord(){
        Random random=new Random();
        WordButton buf=null;
        while(true){
            int index=random.nextInt(MyGridView.COUNTS_WORDS);
            buf=mAllWords.get(index);
            if (buf.mIsVisiable&&!isAnswerWord(buf)){
                return buf;
            }
        }
    }
    /**
     * 找到一个答案
     * index 当前需要填入答案框的索引
     */
    private WordButton findAnswerWord(int index){
        WordButton buf=null;
        for (int i=0;i<MyGridView.COUNTS_WORDS;i++){
            buf=mAllWords.get(i);
            if (buf.mWordString.equals(""+mCurrentSong
            .getNameCharacters()[index])){
                return buf;
            }
        }
        return null;
    }

    /**
     * 判断某个文字是否为答案
     * @return
     */
    private boolean isAnswerWord(WordButton word){
        boolean result=false;
        for(int i=0;i<mCurrentSong.getNameLength();i++){
            if (word.mWordString.equals
                    (""+mCurrentSong.getNameCharacters()[i])){
                result=true;
                break;
            }
        }
        return result;
    }


    /**
     * 增加或者减少指定数量的金币
     * @param data
     * @return  true增加或者减少成功，false失败
     */
    private boolean handleCoins(int data){
        //判断当前总的金币数量是否可被减少
        if (mCurrentCoins+data>=0){
            mCurrentCoins+=data;
            mViewCurrentCoins.setText(mCurrentCoins+"");
            return true;
        }else {
            //金币不够
            return false;
        }
    }
    /**
     * 从配置文件Config.xml读取数值
     */
    private int getDeleteWord(){
        return this.getResources().getInteger(R.integer.pay_delete_word);
    }
    private int getTipCoins(){
        return this.getResources().getInteger(R.integer.pay_tip_answer);
    }

    /**
     * 处理删除待选文字事件
     */
    private void handleDeleteWord(){
        ImageButton button= (ImageButton) findViewById(R.id.btn_delete_word);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOneWord();

            }
        });
    }

    /**
     * 处理提示事件
     */
    private void handleTipAnswer(){
        ImageButton button= (ImageButton) findViewById(R.id.btn_tip_answer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipAnswer();

            }
        });
    }
}
