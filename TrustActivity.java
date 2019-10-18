package com.unipool.unipool;

import android.content.Intent;
import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TrustActivity extends AppCompatActivity {

    boolean user1_like = true;
    boolean user2_like = true;
    boolean user3_like = true;

    String trust_user_1 = "";
    String trust_user_2 = "";
    String trust_user_3 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");
        final String user_1 = beforeIntent.getStringExtra("user_1");
        final String user_2 = beforeIntent.getStringExtra("user_2");
        final String user_3 = beforeIntent.getStringExtra("user_3");
        final String user_4 = beforeIntent.getStringExtra("user_4");
        final String[] U_list = beforeIntent.getStringArrayExtra("U_list");
        final int quantity = beforeIntent.getIntExtra("quantity",1);
        final int isBoard = beforeIntent.getIntExtra("isBoard",0);
        final String Uni = beforeIntent.getStringExtra("Uni");

        final TextView trust_userText_1 = (TextView)findViewById(R.id.trust_userText_1);
        final TextView trust_userText_2 = (TextView)findViewById(R.id.trust_userText_2);
        final TextView trust_userText_3 = (TextView)findViewById(R.id.trust_userText_3);

        final LinearLayout linearLayout_1 = (LinearLayout)findViewById(R.id.LinearLayout_1);
        final LinearLayout linearLayout_2 = (LinearLayout)findViewById(R.id.LinearLayout_2);
        final LinearLayout linearLayout_3 = (LinearLayout)findViewById(R.id.LinearLayout_3);

        final RadioButton user1_like_button = (RadioButton)findViewById(R.id.user1_like_button);
        final RadioButton user1_dislike_button = (RadioButton)findViewById(R.id.user1_dislike_button);
        final RadioButton user2_like_button = (RadioButton)findViewById(R.id.user2_like_button);
        final RadioButton user2_dislike_button = (RadioButton)findViewById(R.id.user2_dislike_button);
        final RadioButton user3_like_button = (RadioButton)findViewById(R.id.user3_like_button);
        final RadioButton user3_dislike_button = (RadioButton)findViewById(R.id.user3_dislike_button);

        final RadioGroup RadioGroup1 = (RadioGroup)findViewById(R.id.RadioGroup1);
        final RadioGroup RadioGroup2 = (RadioGroup)findViewById(R.id.RadioGroup2);
        final RadioGroup RadioGroup3 = (RadioGroup)findViewById(R.id.RadioGroup3);

        final Button TrustOkButton = (Button)findViewById(R.id.TrustOkButton);

        if(quantity==1) {
            Toast.makeText(TrustActivity.this,"목적지에 도착하였습니다. 혼자 탑승은 평가 불가합니다",Toast.LENGTH_LONG).show();
            if(isBoard == 1) {
                Intent MainIntent = new Intent(TrustActivity.this,MainActivity.class);
                MainIntent.putExtra("userID", userID);
                MainIntent.putExtra("U_list",U_list);
                MainIntent.putExtra("Uni",Uni);
                startActivity(MainIntent);
                finish();
            }
            else {
                finish();
            }
        }
        else if(quantity==2) {
            linearLayout_2.setVisibility(View.GONE);
            linearLayout_3.setVisibility(View.GONE);
        }
        else if(quantity==3) {
            linearLayout_3.setVisibility(View.GONE);
        }

        if(user_1.equals(userID)) {
            trust_userText_1.setText(user_2);
            trust_userText_2.setText(user_3);
            trust_userText_3.setText(user_4);
        }
        else if(user_2.equals(userID)) {
            trust_userText_1.setText(user_1);
            trust_userText_2.setText(user_3);
            trust_userText_3.setText(user_4);
        }
        else if(user_3.equals(userID)) {
            trust_userText_1.setText(user_1);
            trust_userText_2.setText(user_2);
            trust_userText_3.setText(user_4);
        }
        else if(user_4.equals(userID)) {
            trust_userText_1.setText(user_1);
            trust_userText_2.setText(user_2);
            trust_userText_3.setText(user_3);
        }

        trust_user_1 = trust_userText_1.getText().toString();
        trust_user_2 = trust_userText_2.getText().toString();
        trust_user_3 = trust_userText_3.getText().toString();

        RadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.user1_like_button)  user1_like = true;
                else if (i==R.id.user1_dislike_button) user1_like = false;
                //Toast.makeText(TrustActivity.this,user1_like+"",Toast.LENGTH_SHORT).show();
            }
        });

        RadioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.user2_like_button)  user2_like = true;
                else if (i==R.id.user2_dislike_button) user2_like = false;
            }
        });

        RadioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.user3_like_button)  user3_like = true;
                else if (i==R.id.user3_dislike_button) user3_like = false;
            }
        });

        TrustOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                if(isBoard == 1) {
                                    Intent MainIntent = new Intent(TrustActivity.this,MainActivity.class);
                                    MainIntent.putExtra("userID", userID);
                                    MainIntent.putExtra("U_list",U_list);
                                    MainIntent.putExtra("Uni",Uni);
                                    startActivity(MainIntent);
                                    finish();
                                }
                                else {
                                    finish();
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                TrustRequest trustRequest = new TrustRequest(trust_user_1,user1_like,trust_user_2,user2_like,trust_user_3,user3_like,quantity,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(TrustActivity.this);
                requestQueue.add(trustRequest);
            }
        });
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
    public void onBackPressed() {

    }
}
