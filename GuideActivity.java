package com.unipool.unipool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class GuideActivity extends AppCompatActivity {

    ImageView cursor_view;
    TextView GuideUniSelectText;
    TextView GuideDepartureSelectText;
    TextView GuideArrivalSelectText;
    TextView GuideFindText;

    int temp = 0;
    float X,Y;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        cursor_view = (ImageView)findViewById(R.id.cursor_view);
        GuideUniSelectText = (TextView)findViewById(R.id.GuideUniSelectText);
        GuideDepartureSelectText = (TextView)findViewById(R.id.GuideDepartureSelectText);
        GuideArrivalSelectText = (TextView)findViewById(R.id.GuideArrivalSelectText);
        GuideFindText = (TextView)findViewById(R.id.GuideFindText);

        init();

        AlertDialog.Builder builder = new AlertDialog.Builder(GuideActivity.this);
        builder.setMessage("반갑습니다.\n유니풀 시작 가이드를 보시려면 '예'를 눌러주세요.")
                .setTitle("유니풀 시작가이드")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GuideStart();
                    }
                })
                .setNegativeButton("스킵하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return super.onTouchEvent(event);
    }

    public void GuideStart() {
        Intent intent = getIntent();
        final float UX = intent.getFloatExtra("UX",0);
        final float UY = intent.getFloatExtra("UY",0);
        final float DX = intent.getFloatExtra("DX",0);
        final float DY = intent.getFloatExtra("DY",0);
        final float AX = intent.getFloatExtra("AX",0);
        final float AY = intent.getFloatExtra("AY",0);
        final float MX = intent.getFloatExtra("MX",0);
        final float MY = intent.getFloatExtra("MY",0);
        Toast.makeText(GuideActivity.this,"UX :"+UX +" UY : " + UY,Toast.LENGTH_SHORT).show();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                switch (temp) {
                    case 0 :
                        cursor_view.animate().translationY(500).withLayer();
                        cursor_view.animate().translationX(500).withLayer();
                        break;
                    case 1 :
                        cursor_view.setImageResource(R.drawable.cursor_click_icon);
                        GuideUniSelectText.setX(650);
                        GuideUniSelectText.setY(500);
                        break;
                    case 2 :
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GuideUniSelectText.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 3 :
                        cursor_view.setImageResource(R.drawable.cursor_icon);
                        cursor_view.animate().translationY(720).withLayer();
                        break;
                    case 4 :
                        cursor_view.setImageResource(R.drawable.cursor_click_icon);
                        GuideDepartureSelectText.setX(650);
                        GuideDepartureSelectText.setY(720);
                        break;
                    case 5 :
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GuideDepartureSelectText.setVisibility(View.VISIBLE);
                            }
                        });
                    case 6 :
                        cursor_view.setImageResource(R.drawable.cursor_icon);
                        cursor_view.animate().translationY(920).withLayer();
                        break;
                    case 7 :
                        cursor_view.setImageResource(R.drawable.cursor_click_icon);
                        GuideArrivalSelectText.setX(650);
                        GuideArrivalSelectText.setY(920);
                        break;
                    case 8 :
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GuideArrivalSelectText.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 9 :
                        cursor_view.setImageResource(R.drawable.cursor_icon);
                        cursor_view.animate().translationY(1150).withLayer();
                        cursor_view.animate().translationX(700).withLayer();
                        break;
                    case 10 :
                        cursor_view.setImageResource(R.drawable.cursor_click_icon);
                        GuideFindText.setX(700);
                        GuideFindText.setY(1250);
                        break;
                    case 11 :
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GuideFindText.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    case 12 :
                        timer.cancel();
                        break;
                }
                temp++;
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,500);
    }

    public void init() {
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        X = display.getWidth() * 0.5f;
        Y = display.getHeight() * 0.5f;
        cursor_view.setX(X);
        cursor_view.setY(Y);

        GuideUniSelectText.setVisibility(View.INVISIBLE);
        GuideDepartureSelectText.setVisibility(View.INVISIBLE);
        GuideArrivalSelectText.setVisibility(View.INVISIBLE);
        GuideFindText.setVisibility(View.INVISIBLE);
    }
}
