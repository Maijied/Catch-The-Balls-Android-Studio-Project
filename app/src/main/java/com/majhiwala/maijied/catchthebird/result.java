package com.majhiwala.maijied.catchthebird;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLevel = (TextView) findViewById(R.id.scoreLevel);
        TextView highScoreLevel = (TextView) findViewById(R.id.highScoreLevel);

        int death = getIntent().getIntExtra("SCORE", 0);
        scoreLevel.setText(death +"");

        SharedPreferences setting  = getSharedPreferences("GAME DATA",Context.MODE_PRIVATE);
        int highScore = setting.getInt("HIGH SCORE", 0);

        if (death> highScore){
            highScoreLevel.setText("HIGH SCORE : " + death);


            //Save
            SharedPreferences.Editor editor = setting.edit();
            editor.putInt("HIGH SCORE", death);
            editor.commit();
        }else {
            highScoreLevel.setText("HIGH SCORE : "+ highScore);
        }
    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), main.class));
    }
    public void mainMenu(View view){
        startActivity(new Intent(getApplicationContext(), start.class));
    }

    //Disable Return Button
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
}
