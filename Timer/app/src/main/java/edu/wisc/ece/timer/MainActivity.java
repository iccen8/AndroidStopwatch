package edu.wisc.ece.timer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button pauseButton;
    private Button resetButton;

    private TextView timeView;

    private Handler hand = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = (TextView) findViewById(R.id.txt_timer);

        startButton = (Button) findViewById(R.id.btn_start);
        pauseButton = (Button) findViewById(R.id.btn_pause);
        resetButton = (Button) findViewById(R.id.btn_reset);

    }

    long sec = 0;
    long msec = 0;
    long startTime = 0;
    long diff = 0;
    long newTime = 0;
    long pauseTime = 0;
    long endPauseTime = 0;

    boolean isPause = false;

    public void startTimer(View arg0){
        new Thread(new Runnable() {
            @Override
            public void run() {
                diff = 0;
                pauseTime = 0;
                endPauseTime = 0;
                isPause = false;
                startTime = System.currentTimeMillis();
                hand.post(calculateTime);
            }
        }).start();
    }

    public void pauseTimer(View arg0){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!isPause){
                    pauseTime = System.currentTimeMillis();
                    hand.removeCallbacks(calculateTime);
                    isPause = true;
                }
                else{
                    endPauseTime = System.currentTimeMillis();
                    diff += (endPauseTime - pauseTime);
                    System.out.println(diff);
                    hand.post(calculateTime);
                    isPause = false;
                }
            }
        }).start();
    }

    public void resetTimer(View arg0){
        new Thread(new Runnable() {
            @Override
            public void run() {
                diff = 0;
                pauseTime = 0;
                endPauseTime = 0;
                isPause = false;
                hand.removeCallbacks(calculateTime);
                timeView.post(new Runnable() {
                    @Override
                    public void run() {
                        timeView.setText("0:0");
                    }
                });
            }
        }).start();
    }


    private Runnable calculateTime = new Runnable() {
        @Override
        public void run() {

            long currentTime = System.currentTimeMillis();
            //System.out.println(diff);
            //newTime = isPause? currentTime - startTime - diff : currentTime - startTime;
            newTime = currentTime - startTime - diff;
            sec = newTime/1000;
            msec = newTime%1000;
            timeView.setText(sec+":"+msec);
            hand.post(this);

        }
    };
}
