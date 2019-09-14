package com.unipool.unipool;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    InputMethodManager inputMethodManager;
    LinearLayout linearLayout;
    EditText Register_idText;
    EditText Register_pwText;
    EditText Register_UniText;
    EditText Register_accountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final String email = getIntent().getStringExtra("email");
        final Button Register_OkButton = (Button)findViewById(R.id.Register_OkButton);
        final Button Register_CancelButton = (Button)findViewById(R.id.Register_CancelButton);
        Register_idText = (EditText)findViewById(R.id.Register_idText);
        Register_pwText = (EditText)findViewById(R.id.Register_pwText);
        Register_UniText = (EditText)findViewById(R.id.Register_UniText);
        Register_accountText = (EditText)findViewById(R.id.Register_accountText);

        linearLayout = (LinearLayout)findViewById(R.id.register_layout);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        linearLayout.setOnClickListener(myClickListener);

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
                                boolean mail = jsonResponse.getBoolean("mail");

                                if (!mail) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("이미 가입된 구글계정 입니다.")
                                            .setNegativeButton("확인", null)
                                            .create()
                                            .show();
                                }
                                else {
                                    if (!overlap) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setMessage("이미 등록된 아이디 입니다.")
                                                .setNegativeButton("다시시도", null)
                                                .create()
                                                .show();
                                    } else {
                                        if (success) {
                                            DBHelper dbHelper = new DBHelper(RegisterActivity.this);
                                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                                            db.execSQL("insert into login_info(userID,userPW,uni) values(?,?,?)",new String[]{userID,userPW,Uni});
                                            db.close();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                            builder.setMessage("회원가입 성공")
                                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(RegisterActivity.this,SplashActivity.class);
                                                            startActivity(intent);
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
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterRequest registerRequest = new RegisterRequest(email,userID, userPW, Uni, Account, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }

    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            hideKeyboard();
            switch (view.getId())
            {
                case R.id.register_layout :
                    break;
            }
        }
    };
    // 뒤로가기 막기
    @Override
    public void onBackPressed() {
    }

    private void hideKeyboard()
    {
        inputMethodManager.hideSoftInputFromWindow(Register_idText.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(Register_pwText.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(Register_UniText.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(Register_accountText.getWindowToken(), 0);
    }

}
