package com.unipool.unipool;

import android.content.Intent;
import android.graphics.Rect;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MatchActivity extends AppCompatActivity {
    Timer timer;
    RequestQueue completeQueue;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        // MainActivity 에서 정보 받아오기
        Intent beforeIntent = getIntent();
        //final int leader = beforeIntent.getIntExtra("quantity",1);
        final String userID = beforeIntent.getStringExtra("userID");
        final String departure = beforeIntent.getStringExtra("departure");
        final String arrival = beforeIntent.getStringExtra("arrival");
        // 위젯 변수
        final ImageView load_image = (ImageView)findViewById(R.id.load_image);
        GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(load_image);
        Glide.with(this).load(R.drawable.load).into(glideDrawableImageViewTarget);

        final TextView match_Text = (TextView) findViewById(R.id.match_Text);
        final TextView match_Quantity_Text = (TextView) findViewById(R.id.matchQuantity_Text);
        final TextView match_leader_Text = (TextView)findViewById(R.id.match_leader_Text);
        final Button Match_Ok_Button = (Button)findViewById(R.id.Match_Ok_Button);
        final Button Match_Cancel_Button = (Button)findViewById(R.id.Match_Cancel_Button);

        completeQueue =  Volley.newRequestQueue(MatchActivity.this);
        // 변수 설정


        match_Text.setText("<매치 됨>");

        Match_Ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(!success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
                                builder.setMessage("매칭 완료 실패")
                                        .setNegativeButton("다시시도",null)
                                        .create()
                                        .show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                CompleteRequest completeRequest = new CompleteRequest(userID,departure,arrival,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(MatchActivity.this);
                requestQueue.add(completeRequest);
            }
        });


        Match_Cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                Toast.makeText(MatchActivity.this,"매칭이 취소되었습니다.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                CancelRequest cancelRequest = new CancelRequest(userID,departure,arrival,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(MatchActivity.this);
                requestQueue.add(cancelRequest);
            }
        });

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            boolean leader = jsonResponse.getBoolean("leader");
                            int quantity = jsonResponse.getInt("quantity");
                            int complete = jsonResponse.getInt("complete");
                            String user_1 = jsonResponse.getString("user_1");
                            String user_2 = jsonResponse.getString("user_2");
                            String user_3 = jsonResponse.getString("user_3");
                            String user_4 = jsonResponse.getString("user_4");
                            if(success) {
                                if(quantity==1)
                                    match_Quantity_Text.setText("1/4");
                                else if(quantity==2)
                                    match_Quantity_Text.setText("2/4");
                                else if(quantity==3)
                                    match_Quantity_Text.setText("3/4");
                                else if(quantity==4)
                                    match_Quantity_Text.setText("4/4");

                                if(leader) {
                                    Match_Ok_Button.setVisibility(View.VISIBLE);
                                    match_leader_Text.setVisibility(View.VISIBLE);
                                }
                                else if(!leader) {
                                    Match_Ok_Button.setVisibility(View.GONE);
                                    match_leader_Text.setVisibility(View.GONE);
                                }
                                if(complete==1 && count ==0) {
                                    count++;
                                    Intent CompleteIntent = new Intent(MatchActivity.this,CompleteActivity.class);
                                    CompleteIntent.putExtra("userID",userID);
                                    CompleteIntent.putExtra("departure",departure);
                                    CompleteIntent.putExtra("arrival",arrival);
                                    CompleteIntent.putExtra("user_1",user_1);
                                    CompleteIntent.putExtra("user_2",user_2);
                                    CompleteIntent.putExtra("user_3",user_3);
                                    CompleteIntent.putExtra("user_4",user_4);
                                    CompleteIntent.putExtra("quantity",quantity);
                                    startActivity(CompleteIntent);
                                    finish();
                                }
                            }
                            else
                                Toast.makeText(MatchActivity.this,"Refresh 실패",Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                //
                RefreshRequest refreshRequest = new RefreshRequest(userID,departure,arrival,responseListener);
                completeQueue.add(refreshRequest);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,1000); // 0초후 1초마다 반복 ms 단위

    }

    @Override
    public  void onBackPressed() {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);
        if(!dialogBounds.contains((int)ev.getX(), (int)ev.getY())){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

}
