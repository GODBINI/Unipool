package com.unipool.unipool;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class BoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    long time = 0;
    RecyclerView board_RecyclerView;
    Board_RecyclerAdapter board_recyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    String user;
    Timer timer;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_draw);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");
        user = userID;

        final String trust = beforeIntent.getStringExtra("trust");
        final String Uni = beforeIntent.getStringExtra("Uni");
        final String[] U_list = beforeIntent.getStringArrayExtra("U_list");
        final Button board_homeButton = (Button)findViewById(R.id.board_homeButton);
        final Button board_boardButton = (Button)findViewById(R.id.board_boardButton);
        final Button Write_Button = (Button)findViewById(R.id.Write_Button);
        final Button refresh_Button = (Button)findViewById(R.id.refresh_Button);
        final Button Remove_Button = (Button)findViewById(R.id.Remove_Button);
        final Button board_complete_button = (Button)findViewById(R.id.board_complete_button);
        final Button user_setting_Button2 = (Button)findViewById(R.id.user_setting_Button2);

        final Spinner board_spinner = (Spinner)findViewById(R.id.board_spinner);
        ArrayAdapter boardSpinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,U_list);
        board_spinner.setAdapter(boardSpinnerAdapter);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.board_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.board_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_headerView = navigationView.getHeaderView(0);

        final TextView board_nav_userID_Text = (TextView)nav_headerView.findViewById(R.id.board_nav_userID_Text);
        final TextView board_nav_userTrust_Text = (TextView)nav_headerView.findViewById(R.id.board_nav_userTrust_Text);
        final TextView nav_title_Text = (TextView)nav_headerView.findViewById(R.id.nav_title_Text);
        final TextView nav_leader_user = (TextView)nav_headerView.findViewById(R.id.nav_leader_user);
        final TextView nav_user_2 = (TextView)nav_headerView.findViewById(R.id.nav_user_2);
        final TextView nav_user_3 = (TextView)nav_headerView.findViewById(R.id.nav_user_3);
        final TextView nav_user_4 = (TextView)nav_headerView.findViewById(R.id.nav_user_4);
        final Button nav_leave_button = (Button)nav_headerView.findViewById(R.id.nav_leave_button);
        final Button nav_chat_button = (Button)nav_headerView.findViewById(R.id.nav_chat_button);
        final LinearLayout party_layout = (LinearLayout)nav_headerView.findViewById(R.id.party_layout);
        final TextView null_Text = (TextView)nav_headerView.findViewById(R.id.null_Text);

        DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                board_nav_userID_Text.setText(" 안녕하세요, " + userID+"님");
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                String title = jsonResponse.getString("title");
                                String leader_user = jsonResponse.getString("leader_user");
                                String user_2 = jsonResponse.getString("user_2");
                                String user_3 = jsonResponse.getString("user_3");
                                String user_4 = jsonResponse.getString("user_4");
                                String trust = jsonResponse.getString("trust");
                                board_nav_userTrust_Text.setText(trust);
                                nav_leader_user.setText(leader_user);
                                nav_title_Text.setText("<"+title+"> 방");
                                nav_user_2.setText(user_2);
                                nav_user_3.setText(user_3);
                                nav_user_4.setText(user_4);
                                if(title.equals("null")) {
                                    party_layout.setVisibility(View.GONE);
                                    null_Text.setVisibility(View.VISIBLE);
                                }
                                else {
                                    party_layout.setVisibility(View.VISIBLE);
                                    null_Text.setVisibility(View.GONE);
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                GetBoardRequest getBoardRequest = new GetBoardRequest(userID,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                requestQueue.add(getBoardRequest);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        };
        drawer.setDrawerListener(drawerListener);

        board_RecyclerView = (RecyclerView)findViewById(R.id.board_RecyclerView);
        board_recyclerAdapter = new Board_RecyclerAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        board_RecyclerView.setLayoutManager(linearLayoutManager);

        board_RecyclerView.setAdapter(board_recyclerAdapter);

        refresh();

        board_homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(BoardActivity.this);
                dialog.setContentView(R.layout.loading);
                ImageView imageView = (ImageView)dialog.findViewById(R.id.loading_image);
                imageView.setImageResource(R.drawable.loading);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Intent board_homeIntent = new Intent(BoardActivity.this,MainActivity.class);
                board_homeIntent.putExtra("userID",userID);
                board_homeIntent.putExtra("Uni",Uni);
                board_homeIntent.putExtra("U_list",U_list);
                startActivity(board_homeIntent);
                overridePendingTransition(R.anim.anim_slide_out_right,R.anim.anim_slide_in_left);
                finish();
            }
        });

        board_boardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BoardActivity.this,"현재 '게시판'상태 입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        Write_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Write_Intent = new Intent(BoardActivity.this,WriteActivity.class);
                Write_Intent.putExtra("userID",userID);
                Write_Intent.putExtra("trust",trust);
                Write_Intent.putExtra("Uni",Uni);
                Write_Intent.putExtra("U_list",U_list);
                startActivity(Write_Intent);
            }
        });

        refresh_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                board_recyclerAdapter.init();
                board_recyclerAdapter.notifyDataSetChanged();
                refresh();
            }
        });

        user_setting_Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
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
                            if(success) {
                                final String apply_user = jsonResponse.getString("apply_user");
                                String trust = jsonResponse.getString("trust");
                                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                                builder.setMessage(apply_user + "님이 신청하였습니다.\n신뢰도 : " + trust +"점")
                                        .setPositiveButton("수락", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonResponse = new JSONObject(response);
                                                            boolean success = jsonResponse.getBoolean("success");
                                                            if(!success)
                                                                Toast.makeText(BoardActivity.this,"승인 실패",Toast.LENGTH_SHORT).show();
                                                        }
                                                        catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };
                                                ApplyAcceptRequest applyAcceptRequest = new ApplyAcceptRequest(apply_user,userID,responseListener);
                                                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                                                requestQueue.add(applyAcceptRequest);
                                            }
                                        })
                                        .setNegativeButton("거절",null)
                                        .create()
                                        .show();
                            }
                            else {
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(BoardActivity.this,"오류 발생",Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                ApplyRefreshRequest applyRefreshRequest = new ApplyRefreshRequest(userID,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                requestQueue.add(applyRefreshRequest);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,500);

        Remove_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                builder.setMessage("작성한 게시글을 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                                                builder.setMessage("삭제 되었습니다.")
                                                        .setPositiveButton("확인",null)
                                                        .create()
                                                        .show();
                                                board_recyclerAdapter.init();
                                                board_recyclerAdapter.notifyDataSetChanged();
                                                refresh();
                                            }
                                            else {
                                                Toast.makeText(BoardActivity.this,"삭제할 게시글이 없습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                BoardRemoveRequest boardRemoveRequest = new BoardRemoveRequest(userID,responseListener);
                                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                                requestQueue.add(boardRemoveRequest);
                            }
                        })
                        .setNegativeButton("아니요",null)
                        .create()
                        .show();
            }
        });

        nav_leave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                builder.setMessage("탈퇴 하시겠습니까?")
                        .setNegativeButton("아니요",null)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                                                builder.setMessage("탈퇴 하였습니다.")
                                                        .setPositiveButton("확인",null)
                                                        .create()
                                                        .show();
                                                drawer.closeDrawer(GravityCompat.START);
                                                board_recyclerAdapter.init();
                                                board_recyclerAdapter.notifyDataSetChanged();
                                                refresh();
                                            }
                                            else {
                                                Toast.makeText(BoardActivity.this,"리더는 글을 삭제해주세요.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                BoardLeaveRequest boardLeaveRequest = new BoardLeaveRequest(userID,responseListener);
                                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                                requestQueue.add(boardLeaveRequest);
                            }
                        })
                        .create()
                        .show();
            }
        });

        board_complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                builder.setMessage("모집을 완료하시겠습니까? \n완료하면 더이상 다른 사람들을 모집할수 없습니다.")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success) {
                                                Toast.makeText(BoardActivity.this,"모집 완료.",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(BoardActivity.this,"파티가 존재하지 않거나 리더가 아닙니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                BoardCompleteRequest boardCompleteRequest = new BoardCompleteRequest(userID,responseListener);
                                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                                requestQueue.add(boardCompleteRequest);
                            }
                        })
                        .setNegativeButton("아니요",null)
                        .create()
                        .show();
            }
        });

        nav_chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                String school = jsonResponse.getString("school");
                                String title = jsonResponse.getString("title");
                                String user_1 = jsonResponse.getString("leader_user");
                                String user_2 = jsonResponse.getString("in_user_2");
                                String user_3 = jsonResponse.getString("in_user_3");
                                String user_4 = jsonResponse.getString("in_user_4");
                                int quantity = jsonResponse.getInt("quantity");
                                Intent BoardCompleteIntent = new Intent(BoardActivity.this,CompleteActivity.class);
                                BoardCompleteIntent.putExtra("departure",school);
                                BoardCompleteIntent.putExtra("arrival",title);
                                BoardCompleteIntent.putExtra("userID",userID);
                                BoardCompleteIntent.putExtra("user_1",user_1);
                                BoardCompleteIntent.putExtra("user_2",user_2);
                                BoardCompleteIntent.putExtra("user_3",user_3);
                                BoardCompleteIntent.putExtra("user_4",user_4);
                                BoardCompleteIntent.putExtra("quantity",quantity);
                                BoardCompleteIntent.putExtra("isBoard",1);
                                BoardCompleteIntent.putExtra("U_list",U_list);
                                BoardCompleteIntent.putExtra("Uni",Uni);
                                startActivity(BoardCompleteIntent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                                builder.setMessage("파티모집을 먼저 완료해 주세요.")
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
                BoardChatRequest boardChatRequest = new BoardChatRequest(userID,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
                requestQueue.add(boardChatRequest);
            }
        });

        for (int i=0; i< U_list.length; i++) {
            if(U_list[i].equals(Uni)){
                board_spinner.setSelection(i);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-time>=1000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<1000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }

    public void refresh() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    int count = 0;
                    String leader_user,school,title,comment,trust,quantity;
                    while (count < jsonArray.length()) {
                        JSONObject jsonObject = jsonArray.getJSONObject(count);
                        leader_user = jsonObject.getString("leader_user");
                        school = jsonObject.getString("school");
                        title = jsonObject.getString("title");
                        comment = jsonObject.getString("comment");
                        trust = jsonObject.getString("trust");
                        quantity = jsonObject.getString("quantity");
                        board_data boardData = new board_data(school,title,comment,leader_user,trust,quantity,user);
                        board_recyclerAdapter.addItem(boardData);
                        board_recyclerAdapter.notifyDataSetChanged();
                        count++;
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        BoardRefreshRequest boardRefreshRequest = new BoardRefreshRequest(responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(BoardActivity.this);
        requestQueue.add(boardRefreshRequest);
    }

    @Override
    public void onDestroy(){
        timer.cancel();
        super.onDestroy();
        dialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(BoardActivity.this,InformChangeActivity.class);
            intent.putExtra("userID",user);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(BoardActivity.this,SuggestActivity.class);
            intent.putExtra("userID",user);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.board_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
