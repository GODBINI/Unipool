package com.unipool.unipool;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class CompleteActivity extends AppCompatActivity {
    public static final String TAG = "100";
    Timer timer;
    Timer timer2;
    int is_board;
    int u_count = 0;
    int temp = 0;
    InputMethodManager inputMethodManager;
    EditText chat_EditText;
    LinearLayout linearLayout;

    ChatRefreshRequest chatRefreshRequest;
    RequestQueue chatQueue;
    RequestQueue completeQueue;
    ArriveRefreshRequest arriveRefreshRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");
        final String departure = beforeIntent.getStringExtra("departure");
        final String arrival = beforeIntent.getStringExtra("arrival");
        final String user_1 = beforeIntent.getStringExtra("user_1");
        final String user_2 = beforeIntent.getStringExtra("user_2");
        final String user_3 = beforeIntent.getStringExtra("user_3");
        final String user_4 = beforeIntent.getStringExtra("user_4");
        final String[] U_list = beforeIntent.getStringArrayExtra("U_list");
        final String Uni = beforeIntent.getStringExtra("Uni");
        final int quantity = beforeIntent.getIntExtra("quantity",1);
        final int isBoard = beforeIntent.getIntExtra("isBoard",0);
        is_board = isBoard;

        final TextView departure_Text = (TextView)findViewById(R.id.departure_Text);
        final TextView arrival_Text = (TextView)findViewById(R.id.arrival_Text);
        final TextView leader_user_Text = (TextView)findViewById(R.id.leader_user_Text);
        final TextView user1_Text = (TextView)findViewById(R.id.user1_Text);
        final TextView user2_Text = (TextView)findViewById(R.id.user2_Text);
        final TextView user3_Text = (TextView)findViewById(R.id.user3_Text);

        final Button arrival_Button = (Button)findViewById(R.id.arrival_Button);
        final Button chat_Button = (Button)findViewById(R.id.chat_Button);

        chat_EditText = (EditText)findViewById(R.id.chat_EditText);
        linearLayout = (LinearLayout)findViewById(R.id.complete_layout);


        final RecyclerView chat_RecyclerView = (RecyclerView)findViewById(R.id.chat_RecyclerView);
        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final Handler handler = new Handler();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ArriveRequest arriveRequest;
                    if(isBoard == 1) {
                        arriveRequest = new ArriveRequest(user_1,user_2,user_3,user_4,isBoard,arrival,responseListener);
                    }
                    else {
                        arriveRequest = new ArriveRequest(user_1,user_2,user_3,user_4,isBoard,departure+user_1,responseListener);
                    }
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
                    ChatInputRequest chatInputRequest;
                    if (isBoard == 1) {
                        chatInputRequest = new ChatInputRequest(userID, arrival, chat_Text, responseListener);
                    }
                    else {
                        chatInputRequest = new ChatInputRequest(userID, departure+user_1, chat_Text, responseListener);
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(CompleteActivity.this);
                    requestQueue.add(chatInputRequest);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chat_RecyclerView.scrollToPosition(recyclerAdapter.getItemCount()-1);
                    }
                }, 300);
            }
        });

        chatQueue = Volley.newRequestQueue(CompleteActivity.this);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                /*if (chatQueue != null) {
                    chatQueue.cancelAll(TAG);
                } */
                temp++;
                System.out.println("counter: "+temp);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("response");
                            int count = 0;
                            boolean myText;
                            String chat_Text,chat_user;
                            recyclerAdapter.init();
                            while(count < jsonArray.length()) {
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                myText = jsonObject.getBoolean("myText");
                                chat_Text = jsonObject.getString("chat_Text");
                                chat_user = jsonObject.getString("chat_user");
                                chat_data chatData = new chat_data(chat_user, chat_Text, myText);
                                recyclerAdapter.addItem(chatData);
                                recyclerAdapter.notifyDataSetChanged();
                                //chat_RecyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
                                count++;
                            }
                            if(u_count == 0) {
                                chat_RecyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
                                u_count++;
                            }
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                };
                if(isBoard == 1) {
                    chatRefreshRequest = new ChatRefreshRequest(userID, arrival, responseListener);
                }
                else {
                    chatRefreshRequest = new ChatRefreshRequest(userID, departure+user_1, responseListener);
                }
                chatRefreshRequest.setTag(TAG);
                chatQueue.add(chatRefreshRequest);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,200);


        completeQueue = Volley.newRequestQueue(CompleteActivity.this);
        TimerTask timerTask2 = new TimerTask() {

            @Override
            public void run() {
                /*if (completeQueue != null) {
                    completeQueue.cancelAll(TAG);
                } */
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            int onArrival = jsonResponse.getInt("onArrival");
                            int onComplete = jsonResponse.getInt("onComplete");
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
                                if(onComplete==2) {
                                    Intent TrustIntent = new Intent(CompleteActivity.this, TrustActivity.class);
                                    TrustIntent.putExtra("userID",userID);
                                    TrustIntent.putExtra("user_1",user_1);
                                    TrustIntent.putExtra("user_2",user_2);
                                    TrustIntent.putExtra("user_3",user_3);
                                    TrustIntent.putExtra("user_4",user_4);
                                    TrustIntent.putExtra("quantity",quantity);
                                    TrustIntent.putExtra("isBoard",isBoard);
                                    TrustIntent.putExtra("U_list",U_list);
                                    TrustIntent.putExtra("Uni",Uni);
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
                arriveRefreshRequest = new ArriveRefreshRequest(userID,responseListener);
                arriveRefreshRequest.setTag(TAG);
                completeQueue.add(arriveRefreshRequest);
            }
        };
        timer2 = new Timer();
        timer2.schedule(timerTask2,0,1000);

    }

    @Override
    public void onBackPressed() {
        if(is_board == 1) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy(){
        Log.d("CompleteAc","onDestroy()");
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }

}
