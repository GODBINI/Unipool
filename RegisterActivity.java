package com.unipool.unipool;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button Register_OkButton = (Button)findViewById(R.id.Register_OkButton);
        final Button Register_CancelButton = (Button)findViewById(R.id.Register_CancelButton);
        final EditText Register_idText = (EditText)findViewById(R.id.Register_idText);
        final EditText Register_pwText = (EditText)findViewById(R.id.Register_pwText);
        final EditText Register_UniText = (EditText)findViewById(R.id.Register_UniText);
        final EditText Register_accountText = (EditText)findViewById(R.id.Register_accountText);


        Register_CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Register_OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userID = Register_idText.getText().toString();
                final String userPW = Register_pwText.getText().toString();
                final String Uni = Register_UniText.getText().toString();
                final String Account = Register_accountText.getText().toString();
                if (userID.trim().equals(""))
                    Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                else if (userPW.trim().equals(""))
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                else if (Uni.trim().equals(""))
                    Toast.makeText(RegisterActivity.this, "대학교 명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else if (userID.trim().length() < 4 || userID.length() > 19)
                    Toast.makeText(RegisterActivity.this, "ID는 4~20자의 문자만 사용가능합니다.", Toast.LENGTH_SHORT).show();
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean overlap = jsonResponse.getBoolean("overlap");
                                boolean success = jsonResponse.getBoolean("success");

                                if (!overlap) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("이미 등록된 아이디 입니다.")
                                            .setNegativeButton("다시시도", null)
                                            .create()
                                            .show();
                                } else {
                                    if (success) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("회원가입 성공")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                })
                                                .create()
                                                .show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("회원가입 실패!")
                                                .setNegativeButton("다시시도", null)
                                                .create()
                                                .show();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(userID, userPW, Uni, Account, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }

    // 뒤로가기 막기
    @Override
    public void onBackPressed() {
    }

}
