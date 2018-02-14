package com.majhiwala.maijied.catchthebird;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLevel;
    private TextView startLevel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    //Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;


    //Position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    //Speed
    private int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;

    //Score
    private  int score = 0;
    private int death =0;

    //Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private soundPlayer sound;

    //Status Check
    private boolean action_flg  = false;
    private boolean start_flg = false;
    private boolean pause_flg = false;

    //Button
    private Button pauseBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new soundPlayer(this);

        scoreLevel = (TextView) findViewById(R.id.scoreLevel);
        startLevel = (TextView) findViewById(R.id.startLevel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);
        pauseBtn = (Button) findViewById(R.id.pauseBtn);

        //Get screen Size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);


        screenHeight = size.y;
        screenWidth = size.x;


        //Now
        //Nexus4 width:764 height:1184
        boxSpeed = Math.round(screenHeight / 60F);//  1184/60=19.733 --->20
        orangeSpeed = Math.round(screenWidth / 60F);//  768/60=12.8 ---->13
        pinkSpeed = Math.round(screenWidth / 36F);//  768/36=21.311 ---->21
        blackSpeed = Math.round(screenWidth / 45F);//  764/45=17.06 ---->17

//        Log.v("SPEED_BOX", boxSpeed+"");
//        Log.v("SPEED_ORANGE", orangeSpeed+"");
//        Log.v("SPEED_PINK", pinkSpeed+"");
//        Log.v("SPEED_BLACK", blackSpeed+"");


        //Move out of screen
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);




        scoreLevel.setText("Score : 0");




    }

    public void changePos() {

        hitCheck();

        //Orange
        orangeX -= orangeSpeed;
        if (orangeX < 0){
            orangeX  = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));

        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //Black
        blackX -= blackSpeed;
        if (blackX < 0){
            blackX  = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));

        }
        black.setX(blackX);
        black.setY(blackY);

        //pink
        pinkX -= pinkSpeed;
        if (pinkX < 0){
            pinkX  = screenWidth + 5000;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));

        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //MOve box
        if (action_flg == true){
            //Touching
            boxY -= boxSpeed;
        }else{
            //Releasing
            boxY += boxSpeed;
        }

        if (boxY < 0) boxY = 0;
        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;


        box.setY(boxY);
        scoreLevel.setText("Score : "+ score);
    }

    public void hitCheck(){
        //Orange
        int orangeCenterX = orangeX + orange.getWidth() /2;
        int orangeCenterY = orangeY + orange.getHeight() /2;

        if (0 <= orangeCenterX && orangeCenterX <= boxSize &&
        boxY<= orangeCenterY && orangeCenterY <= boxY + boxSize ){

            score +=10;

            orangeY = -10;
            sound.playHitSound();
        }
        //pink
        int pinkCenterX = pinkX + pink.getWidth() /2;
        int pinkCenterY = pinkY + pink.getHeight() /2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize &&
                boxY<= pinkCenterY && pinkCenterY <= boxY + boxSize ) {

            score += 20;
            pinkY = -10;
            sound.playHitSound();
        }

        //Black
        int blackCenterX = blackX + black.getWidth() /2;
        int blackCenterY = blackY + black.getHeight() /2;

        if (0 <= blackCenterX && blackCenterX <= boxSize &&
                boxY<= blackCenterY && blackCenterY <= boxY + boxSize )
        {

            score -= 50;
            blackY = +5;
            sound.playOverSound();


        }
        if(death<score)
        {
            death = score;
        }
       if(0 <= blackCenterX && blackCenterX <= boxSize &&
               boxY<= blackCenterY && blackCenterY <= boxY + boxSize && score <=0)
        {

            //Stop Timer
            timer.cancel();
            timer = null;
            sound.playOverSound();

            //Show Score
            Intent intent = new Intent(getApplicationContext(),result.class);
            intent.putExtra("SCORE", death);
            startActivity(intent);
        }



    }


    public boolean onTouchEvent(MotionEvent me){
        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();
            boxSize = box.getHeight();

            startLevel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);


        }else {

            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }

        }


        return true;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction()== KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void pausePushed(View view)
    {
        if (pause_flg == false)
        {
            pause_flg = true;

            //Timer
            timer.cancel();
            timer = null;

            //Change button Text
            pauseBtn.setText("START");

        }
        else
        {
            pause_flg = false;

            //Change Button Text
            pauseBtn.setText("PAUSE");

            //Create and start the timer
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        }
    }

}
