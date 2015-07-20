package com.mygithubb.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.mygithubb.game.R.id.button;
import static com.mygithubb.game.R.id.text;


public class MainActivity extends Activity {

    private TextView text_info;
    private TextView text_score;
    private ImageView text_pic;
    private TextView text_time;
    private Button btn;
    private String[] directs = {"left", "right", "down", "up"};
    private Boolean isStop = false;


    private float startX, startY, offsetX, offsetY;


    private int scores  = 0;
    private int time  = 30;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_info = (TextView) findViewById(R.id.info);
        text_score = (TextView) findViewById(R.id.score);
        text_pic = (ImageView) findViewById(R.id.pic);
        text_time = (TextView) findViewById(R.id.text_time);
        btn = (Button) findViewById(R.id.button);


    /*
    构建动画对象。
     */
        final Animation toLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.left_anim);
        final Animation toRightAnimation = AnimationUtils.loadAnimation(this, R.anim.right_anim);
        final Animation toUpAnimation = AnimationUtils.loadAnimation(this, R.anim.up_anim);
        final Animation toDownAnimation = AnimationUtils.loadAnimation(this, R.anim.down_anim);

        System.out.println("test");
        /*
         给动画注册监听器
        */
        toLeftAnimation.setAnimationListener(new myAnimationListener());
        toRightAnimation.setAnimationListener(new myAnimationListener());
        toUpAnimation.setAnimationListener(new myAnimationListener());
        toDownAnimation.setAnimationListener(new myAnimationListener());

        changeDirect();

        /*
        对屏幕滑动的监测,现在如果操作错误的话，还可以继续操作。
         */
        text_pic.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -8) {
                                if (text_info.getText() == "left") {
                                    text_pic.startAnimation(toLeftAnimation);
                                    getScore();
                                    changeDirect();
                                } else {
                                    System.out.println("please retry ,your final score is " + scores);
                                    showAlert().show();
                                    isStop = true;
                                }

                            } else if (offsetX > 8) {
                                if (text_info.getText() == "right") {
                                    text_pic.startAnimation(toRightAnimation);
                                    getScore();
                                    changeDirect();
                                } else {
                                    System.out.println("please retry,  your final score is " + scores);
                                    showAlert().show();
                                    isStop = true;
                                }

                            }
                        } else {
                            if (offsetY < -8) {
                                if (text_info.getText() == "up") {
                                    text_pic.startAnimation(toUpAnimation);
                                    getScore();
                                    changeDirect();
                                } else {
                                    System.out.println("please retry , your final score is " + scores);
                                    showAlert().show();
                                    isStop = true;
                                }

                            } else if (offsetY > 8) {
                                if (text_info.getText() == "down") {

                                    text_pic.startAnimation(toDownAnimation);
                                    getScore();
                                    changeDirect();
                                } else {
                                    System.out.println("please retry , your final score is " + scores);
                                    showAlert().show();
                                    isStop = true;
                                }


                            }
                        }
                        break;
                }
                return true;
            }
        });

        /*
        处理定时器
         */

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Thread myThread = new Thread(new MyThread());
                myThread.start();
                btn.setVisibility(View.INVISIBLE);
            }
        });

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    text_time.setText(Integer.toString(time--));
                    super.handleMessage(msg);
                    break;
            }
            if (time == 0) {
                showAlert().show();
            }


        }
    };


    class MyThread implements Runnable {
        public void run() {
            while (time > 0 && !isStop) {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    Toast.makeText(MainActivity.this, "something wrong", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    /*
    在四个方向中随机生成一个。
     */
    public void changeDirect() {
        text_info.setText(directs[(int) (Math.random() * 4)]);
    }

    /*
    显示得到的分数。
     */
    public void getScore() {
        scores++;
        text_score.setText(scores + "");

    }

    /*
    游戏失败之后，弹出提示框。
     */
    public AlertDialog.Builder showAlert() {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(this);
        myAlertBuilder.setCancelable(false);//可以设置在对话框外点击，对话框不消失。
        myAlertBuilder.setTitle("failed");
        myAlertBuilder.setMessage("your scores is  " + scores);
        myAlertBuilder.setPositiveButton("try again", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                retry();
            }
        });
        myAlertBuilder.setNegativeButton("quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return myAlertBuilder;
    }

    //重新开始游戏。
    private void retry() {
        time = 30;
        scores = 0;
        onStart();
        isStop = false;
        changeDirect();
        btn.setVisibility(View.VISIBLE);
        text_time.setText(time + "");
        text_score.setText(scores + "");
    }

    /*
 用来监听每一个动画开始，重复，结束之后的动作。
    */
    class myAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            text_pic.clearAnimation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

/*
定时器
 */

}
