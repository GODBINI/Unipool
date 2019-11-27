package com.unipool.unipool;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Context context = getApplicationContext();
        final EditText id_inputText = (EditText)findViewById(R.id.id_inputText);
        final EditText pw_inputText = (EditText)findViewById(R.id.pw_inputText);
        final Button RegisterButton = (Button)findViewById(R.id.RegisterButton);
        final Button LoginButton = (Button)findViewById(R.id.LoginButton);
        final StringList stringList = new StringList(context);

        final DBHelper dbHelper = new DBHelper(LoginActivity.this);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RegisterIntent = new Intent(LoginActivity.this,GoogleLogin.class);
                startActivityForResult(RegisterIntent,1);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String userID = id_inputText.getText().toString();
               final String userPW = pw_inputText.getText().toString();
                Response.Listener<String> responseListener= new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            String Uni = jsonResponse.getString("Uni");
                            if(success) {
                                SQLiteDatabase wdb = dbHelper.getWritableDatabase();
                                wdb.execSQL("insert into login_info(userID,userPW,uni,map) values(?,?,?,1)",new String[]{userID,userPW,Uni});
                                wdb.close();
                                Intent LoginIntent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginIntent.putExtra("userID",userID);
                                LoginIntent.putExtra("Uni",Uni);
                                LoginIntent.putExtra("U_list",stringList.U_list);
                                startActivity(LoginIntent);
                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("<로그인 실패> \n아이디와 비밀번호를 확인해주세요.")
                                        .setNegativeButton("다시시도",null)
                                        .create()
                                        .show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this,"죄송합니다 현재 서버 점검시간입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID,userPW,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(loginRequest);
            }
        });
    }
}
