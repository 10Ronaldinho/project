package com.mygithubb.game;

import android.app.Activity;


import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;


public class MainActivity extends Activity{


    private TextView text_info;
    private TextView text_score;
    private RelativeLayout relativeLayout;
    private TextView text_pic;

    private float startX,startY,offsetX,offsetY;
    String info = "";
    String score = "";

    private int scores = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_info =(TextView)findViewById(R.id.info);
        text_score =(TextView)findViewById(R.id.score);
        relativeLayout = (RelativeLayout)findViewById(R.id.rela);
        text_pic = (TextView)findViewById(R.id.pic);


        changeDirect();

        text_pic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX()-startX;
                        offsetY = event.getY()-startY;

                        if(Math.abs(offsetX)>Math.abs(offsetY)){
                            if(offsetX<-8){
                                if(text_info.getText()=="left"){

                                     getScore();
                                    changeDirect();
                                }else
                                System.out.println("please retry £¬ your final score is "+scores);

                            }else if (offsetX>8){
                                if(text_info.getText()=="right"){

                                    getScore();
                                    changeDirect();
                                }else
                                    System.out.println("please retry £¬ your final score is "+scores);

                            }
                        }else{
                            if(offsetY<-8){
                                if(text_info.getText()=="up"){

                                    getScore();
                                    changeDirect();
                                }else
                                    System.out.println("please retry £¬ your final score is "+scores);

                            }else if(offsetY>8){
                                if(text_info.getText()=="down"){

                                    getScore();
                                    changeDirect();
                                }else
                                    System.out.println("please retry £¬ your final score is "+scores);


                            }
                        }
                        break;
                }
                return true;
            }
        });
    }
    String [] directs = {"left","right","down","up"};

    public void changeDirect(){

        text_info.setText(directs[(int)(Math.random()*4)]);
    }

    public void getScore(){
        scores++;
        text_score.setText(scores+"");
    }
}
