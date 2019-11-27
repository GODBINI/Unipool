package com.unipool.unipool;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Context context = getApplicationContext();
        final StringList stringList = new StringList(context);

        final DBHelper dbHelper = new DBHelper(SplashActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select userID,userPW from login_info",null);

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(cursor.getCount() != 0) {
            final String userID;
            String user = "";
            String userPW = "";
            while(cursor.moveToNext()) {
                user = cursor.getString(0);
                userPW = cursor.getString(1);
            }
            userID = user;
            Response.Listener<String> responseListener= new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String Uni = jsonResponse.getString("Uni");
                        if(success) {
                            Intent LoginIntent = new Intent(SplashActivity.this, MainActivity.class);
                            LoginIntent.putExtra("userID",userID);
                            LoginIntent.putExtra("U_list",stringList.U_list);
                            LoginIntent.putExtra("Uni",Uni);
                            startActivity(LoginIntent);
                            finish();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                            builder.setMessage("<로그인 실패> \n아이디와 비밀번호를 확인해주세요.")
                                    .setNegativeButton("확인",null)
                                    .create()
                                    .show();
                            Intent SplashIntent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(SplashIntent);
                            finish();
                        }
                    }
                    catch (JSONException e) {
                        /*e.printStackTrace();
                        Toast.makeText(SplashActivity.this,"죄송합니다 현재 서버 점검시간입니다.",Toast.LENGTH_SHORT).show(); */
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(userID,userPW,responseListener);
            RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);
            requestQueue.add(loginRequest);
        }
        else {
            Intent SplashIntent = new Intent(this, LoginActivity.class);
            startActivity(SplashIntent);
            finish();
        }
    }
}
