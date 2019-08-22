package com.unipool.unipool;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class InformChangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_change);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");

        final EditText newPW_Text = (EditText)findViewById(R.id.newPW_Text);
        final EditText newUni_Text = (EditText)findViewById(R.id.newUni_Text);
        final Button ChangePW_Button = (Button)findViewById(R.id.ChangePW_Button);
        final Button ChangeUni_Button = (Button)findViewById(R.id.ChangeUni_Button);
        final Button ChangeOk_Button = (Button)findViewById(R.id.ChangeOk_Button);

        ChangeOk_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ChangePW_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPW = newPW_Text.getText().toString();
                if (newPW.trim().equals("")) {
                    Toast.makeText(InformChangeActivity.this, "공백은 불가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (!success)
                                    Toast.makeText(InformChangeActivity.this, "오류발생", Toast.LENGTH_SHORT).show();
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InformChangeActivity.this);
                                    builder.setMessage("변경 완료")
                                            .setPositiveButton("확인",null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ChangePwRequest changePwRequest = new ChangePwRequest(userID, newPW, responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(InformChangeActivity.this);
                    requestQueue.add(changePwRequest);
                }
            }
        });

        ChangeUni_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUni = newUni_Text.getText().toString();
                if (newUni.trim().equals("")) {
                    Toast.makeText(InformChangeActivity.this, "공백은 불가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (!success)
                                    Toast.makeText(InformChangeActivity.this, "오류발생", Toast.LENGTH_SHORT).show();
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InformChangeActivity.this);
                                    builder.setMessage("변경 완료")
                                            .setPositiveButton("확인",null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ChangeUniRequest changeUniRequest = new ChangeUniRequest(userID, newUni, responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(InformChangeActivity.this);
                    requestQueue.add(changeUniRequest);
                }
            }
        });

    }
}
