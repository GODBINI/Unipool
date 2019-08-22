package com.unipool.unipool;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SuggestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");

        final EditText s_university_Text = (EditText)findViewById(R.id.s_university_Text);
        final EditText s_departure_Text = (EditText)findViewById(R.id.s_departure_Text);
        final EditText s_arrival_Text = (EditText)findViewById(R.id.s_arrival_Text);
        final Button Suggest_Button = (Button)findViewById(R.id.Suggest_Button);
        final Button SuggestOk_Button = (Button)findViewById(R.id.SuggestOk_Button);

        Suggest_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String university = s_university_Text.getText().toString();
                String departure = s_departure_Text.getText().toString();
                String arrival = s_arrival_Text.getText().toString();
                if(university.trim().equals("")) {
                    Toast.makeText(SuggestActivity.this,"대학교 명을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(departure.trim().equals("")) {
                    Toast.makeText(SuggestActivity.this,"출발지를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(arrival.trim().equals("")) {
                    Toast.makeText(SuggestActivity.this,"도착지를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SuggestActivity.this);
                                            builder.setMessage("유저분의 의견이 전달되었습니다.")
                                                .setPositiveButton("확인",null)
                                                .create()
                                                .show();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    SuggestRequest suggestRequest = new SuggestRequest(userID,university,departure,arrival,responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(SuggestActivity.this);
                    requestQueue.add(suggestRequest);
                }
            }
        });

        SuggestOk_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
