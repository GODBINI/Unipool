package com.unipool.unipool;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");
        final String trust = beforeIntent.getStringExtra("trust");
        final Button Write_Cancel_Button = (Button)findViewById(R.id.Write_Cancel_Button);
        final Button Write_Ok_Button = (Button)findViewById(R.id.Write_Ok_Button);
        final EditText School_Text = (EditText)findViewById(R.id.School_Text);
        final EditText Title_Text = (EditText)findViewById(R.id.Title_Text);
        final EditText Comment_Text = (EditText)findViewById(R.id.Comment_Text);

        Write_Cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Write_Ok_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String school = School_Text.getText().toString();
                String title = Title_Text.getText().toString();
                String comment = Comment_Text.getText().toString();
                if(school.trim().equals("") || title.trim().equals("") || comment.trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                    builder.setMessage("학교,제목,내용은 필수입력입니다.")
                            .setPositiveButton("확인",null)
                            .create()
                            .show();
                }
                else if(title.trim().length() < 6 || title.length() > 31) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                    builder.setMessage("제목은 6~30자 사이로 작성해주세요.")
                            .setPositiveButton("확인",null)
                            .create()
                            .show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                boolean overlap = jsonResponse.getBoolean("overlap");
                                if(success){
                                    Toast.makeText(WriteActivity.this,"작성 완료!",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    if(overlap) {
                                        Toast.makeText(WriteActivity.this,"이미 작성한 게시글이 존재하거나 모집글에 속한상태입니다.",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(WriteActivity.this,"같은제목을 가진 모집글이 이미 존재합니다!\n제목을 변경해주세요.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    WriteRequest writeRequest = new WriteRequest(userID,school,title,comment,responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(WriteActivity.this);
                    requestQueue.add(writeRequest);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
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
}
