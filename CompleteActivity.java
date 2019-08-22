package com.unipool.unipool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CompleteActivity extends AppCompatActivity {
    Timer timer;
    Timer timer2;
    String g_userID,g_user_1,g_user_2,g_user_3,g_user_4;
    int g_quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");
        String departure = beforeIntent.getStringExtra("departure");
        String arrival = beforeIntent.getStringExtra("arrival");
        final String user_1 = beforeIntent.getStringExtra("user_1");
        final String user_2 = beforeIntent.getStringExtra("user_2");
        final String user_3 = beforeIntent.getStringExtra("user_3");
        final String user_4 = beforeIntent.getStringExtra("user_4");
        final int quantity = beforeIntent.getIntExtra("quantity",1);
        final int isBoard = beforeIntent.getIntExtra("isBoard",0);
        g_userID = userID; g_user_1 = user_1; g_user_2 = user_2; g_user_3 = user_3; g_user_4 = user_4; g_quantity = quantity;

        final TextView departure_Text = (TextView)findViewById(R.id.departure_Text);
        final TextView arrival_Text = (TextView)findViewById(R.id.arrival_Text);
        final TextView leader_user_Text = (TextView)findViewById(R.id.leader_user_Text);
        final TextView user1_Text = (TextView)findViewById(R.id.user1_Text);
        final TextView user2_Text = (TextView)findViewById(R.id.user2_Text);
        final TextView user3_Text = (TextView)findViewById(R.id.user3_Text);

        final Button arrival_Button = (Button)findViewById(R.id.arrival_Button);
        final Button chat_Button = (Button)findViewById(R.id.chat_Button);

        final EditText chat_EditText = (EditText)findViewById(R.id.chat_EditText);

        final RecyclerView chat_RecyclerView = (RecyclerView)findViewById(R.id.chat_RecyclerView);
        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        chat_RecyclerView.setLayoutManager(linearLayoutManager);

        chat_RecyclerView.setAdapter(recyclerAdapter);

        departure_Text.setText(departure);
        arrival_Text.setText(arrival);
        leader_user_Text.setText(user_1);
        user1_Text.setText(user_2);
        user2_Text.setText(user_3);
        user3_Text.setText(user_4);

        arrival_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userID.equals(user_1)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success) {
                                    Intent TrustIntent = new Intent(CompleteActivity.this, TrustActivity.class);
                                    TrustIntent.putExtra("userID",userID);
                                    TrustIntent.putExtra("user_1",user_1);
                                    TrustIntent.putExtra("user_2",user_2);
                                    TrustIntent.putExtra("user_3",user_3);
                                    TrustIntent.putExtra("user_4",user_4);
                                    TrustIntent.putExtra("quantity",quantity);
                                    TrustIntent.putExtra("isBoard",isBoard);
                                    //startActivity(TrustIntent);
                                    //finish();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ArriveRequest arriveRequest = new ArriveRequest(user_1,user_2,user_3,user_4,responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(CompleteActivity.this);
                    requestQueue.add(arriveRequest);
                }
                else
                    Toast.makeText(CompleteActivity.this,"리더만 도착종료 가능합니다.",Toast.LENGTH_SHORT).show();
            }
        });

        chat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String chat_Text = chat_EditText.getText().toString();
                if (chat_Text.trim().equals("")) {
                    Toast.makeText(CompleteActivity.this, "공백은 전송할수없습니다.", Toast.LENGTH_SHORT).show();
                    chat_EditText.setText("");
                } else {
                    chat_EditText.setText("");
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (!success) {
                                    Toast.makeText(CompleteActivity.this, "채팅입력 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ChatInputRequest chatInputRequest = new ChatInputRequest(userID, chat_Text, responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(CompleteActivity.this);
                    requestQueue.add(chatInputRequest);
                }
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
                            boolean myText = jsonResponse.getBoolean("myText");
                            String chat_Text = jsonResponse.getString("chat_Text");
                            String chat_user = jsonResponse.getString("chat_user");

                            if(success) {
                                //Toast.makeText(CompleteActivity.this,chat_user+","+chat_Text,Toast.LENGTH_SHORT).show();
                                chat_data chatData = new chat_data(chat_user,chat_Text,myText);
                                recyclerAdapter.addItem(chatData);
                                recyclerAdapter.notifyDataSetChanged();
                                chat_RecyclerView.scrollToPosition(recyclerAdapter.getItemCount()-1);
                            }
                            else {
                                //Toast.makeText(CompleteActivity.this,"채팅 서버 연결실패",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                };
                ChatRefreshRequest chatRefreshRequest = new ChatRefreshRequest(userID,user_1,user_2,user_3,user_4,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(CompleteActivity.this);
                requestQueue.add(chatRefreshRequest);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,500);

        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            int onArrival = jsonResponse.getInt("onArrival");
                            if(success) {
                                if(onArrival==1) {
                                    Intent TrustIntent = new Intent(CompleteActivity.this, TrustActivity.class);
                                    TrustIntent.putExtra("userID",userID);
                                    TrustIntent.putExtra("user_1",user_1);
                                    TrustIntent.putExtra("user_2",user_2);
                                    TrustIntent.putExtra("user_3",user_3);
                                    TrustIntent.putExtra("user_4",user_4);
                                    TrustIntent.putExtra("quantity",quantity);
                                    TrustIntent.putExtra("isBoard",isBoard);
                                    startActivity(TrustIntent);
                                    finish();
                                }
                            }
                            else {

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ArriveRefreshRequest arriveRefreshRequest = new ArriveRefreshRequest(userID,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(CompleteActivity.this);
                requestQueue.add(arriveRefreshRequest);
            }
        };
        timer2 = new Timer();
        timer2.schedule(timerTask2,0,1000);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onDestroy(){
        Log.d("CompleteAc","onDestroy()");
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d("CompleteAc","onPause()");
        /*if(g_userID.equals(g_user_1)) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success) {
                            Intent TrustIntent = new Intent(CompleteActivity.this, TrustActivity.class);
                            TrustIntent.putExtra("userID",g_userID);
                            TrustIntent.putExtra("user_1",g_user_1);
                            TrustIntent.putExtra("user_2",g_user_2);
                            TrustIntent.putExtra("user_3",g_user_3);
                            TrustIntent.putExtra("user_4",g_user_4);
                            TrustIntent.putExtra("quantity",g_quantity);
                            //startActivity(TrustIntent);
                            //finish();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ArriveRequest arriveRequest = new ArriveRequest(g_user_1,g_user_2,g_user_3,g_user_4,responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(CompleteActivity.this);
            requestQueue.add(arriveRequest);
        } */
        super.onPause();
    }
}
