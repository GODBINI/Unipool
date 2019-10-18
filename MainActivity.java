package com.unipool.unipool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private long time= 0;
    private boolean select = false;
    private String departure;
    private String arrival;
    private int uni;
    private double departure_latitude;
    private double departure_longitude;
    private double arrival_latitude;
    private double arrival_longitude;
    float pressedX;
    String userID2,Uni2;
    String trust;
    MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
    Dialog dialog;
    StringList stringList;
    Spinner Uni_Spinner;
    Spinner Departure_Spinner;
    Spinner Arrival_Spinner;
    Button Match_Button;

    RequestQueue quickchatQueue;
    RequestQueue getTrustQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        //sqlite 초기화
        /*final DBHelper dbHelper = new DBHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from login_info");
        db.close(); */

        //변수 선언
        Context context = getApplicationContext();
        stringList = new StringList(context);

        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");
        final String Uni = beforeIntent.getStringExtra("Uni");
        final String[] U_list = beforeIntent.getStringArrayExtra("U_list");
        userID2 = userID;
        Uni2 = Uni;
        final Button home_homeButton = (Button)findViewById(R.id.home_homeButton);
        final Button home_boardButton = (Button)findViewById(R.id.home_boardButton);
        Match_Button = (Button)findViewById(R.id.Match_Button);
        final Button user_setting_Button = (Button)findViewById(R.id.user_setting_Button);
        final TextView home_boardText = (TextView)findViewById(R.id.home_boardText);
        Uni_Spinner = (Spinner)findViewById(R.id.Uni_Spinner);
        Departure_Spinner = (Spinner)findViewById(R.id.Departure_Spinner);
        Arrival_Spinner = (Spinner)findViewById(R.id.Arrival_Spinner);
        final LatLngData latLngData = new LatLngData();

        getTrustQueue = Volley.newRequestQueue(MainActivity.this);
        quickchatQueue = Volley.newRequestQueue(MainActivity.this);

        ArrayAdapter UniSpinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,U_list);
        Uni_Spinner.setAdapter(UniSpinnerAdapter);
        // 네비게이션
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_headerView = navigationView.getHeaderView(0);
        final TextView nav_userID_Text = (TextView)nav_headerView.findViewById(R.id.nav_userID_Text);
        final TextView nav_userTrust_Text = (TextView)nav_headerView.findViewById(R.id.nav_userTrust_Text);
        final Button main_chat_Button = (Button)nav_headerView.findViewById(R.id.main_nav_chat_button);
        final Button inform_Button = (Button)nav_headerView.findViewById(R.id.inform_Button);
        final Button suggestion_Button = (Button)nav_headerView.findViewById(R.id.suggestion_Button);
        final ImageView navInfoImage = (ImageView)nav_headerView.findViewById(R.id.navInfoImage);
        final LinearLayout MainLayout = (LinearLayout)findViewById(R.id.MainLayout);

        //GuideIntent();

        DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                nav_userID_Text.setText("  "+userID);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            trust = jsonResponse.getString("trust");
                            if(success) {
                                nav_userTrust_Text.setText(" "+trust);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                GetTrustRequest getTrustRequest = new GetTrustRequest(userID,responseListener);
                getTrustQueue.add(getTrustRequest);
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        };
        drawer.setDrawerListener(drawerListener);
        //


        user_setting_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        // 버튼클릭 리스너
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.loading);
                ImageView imageView = (ImageView)dialog.findViewById(R.id.loading_image);
                imageView.setImageResource(R.drawable.loading);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Intent home_boardIntent = new Intent(MainActivity.this, BoardActivity.class);
                home_boardIntent.putExtra("userID",userID);
                home_boardIntent.putExtra("trust",trust);
                home_boardIntent.putExtra("Uni",Uni);
                home_boardIntent.putExtra("U_list",U_list);
                startActivity(home_boardIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
            }
        };

        home_boardButton.setOnClickListener(onClickListener);
        home_boardText.setOnClickListener(onClickListener);

        home_homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"현재 '홈'상태 입니다.",Toast.LENGTH_SHORT).show();
            }
        });

        Match_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!select) {
                    Toast.makeText(MainActivity.this,"출발지와 도착지를 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                boolean onMatch = jsonResponse.getBoolean("onMatch");
                                int quantity = jsonResponse.getInt("quantity");
                                if(onMatch) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("이미 매칭 진행중입니다.")
                                            .setNegativeButton("확인", null)
                                            .create()
                                            .show();
                                }
                                else {
                                    if (success) {
                                        Intent match_Intent = new Intent(MainActivity.this, MatchActivity.class);
                                        match_Intent.putExtra("userID", userID);
                                        match_Intent.putExtra("departure", departure);
                                        match_Intent.putExtra("arrival", arrival);
                                        match_Intent.putExtra("quantity", quantity);
                                        startActivity(match_Intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setMessage("매칭 진입 실패")
                                                .setNegativeButton("확인", null)
                                                .create()
                                                .show();
                                    }
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    MatchRequest matchRequest = new MatchRequest(userID,departure,arrival,responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(matchRequest);
                }
             }
        });
        // 스피너 선택 리스너
        Uni_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter DepartureAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, stringList.Departure_list[i]);
                ArrayAdapter ArrivalAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, stringList.Arrival_list[i]);
                uni = i;
                Departure_Spinner.setAdapter(DepartureAdapter);
                Arrival_Spinner.setAdapter(ArrivalAdapter);
                if(i==0) {
                    select = false;
                }
                else
                    select = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Departure_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                departure = "" + Departure_Spinner.getItemAtPosition(i);
                departure_latitude = latLngData.Departure_latitude[uni][i];
                departure_longitude = latLngData.Departure_longitude[uni][i];
                mapFragment = MapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
                mapFragment.getMapAsync(MainActivity.this);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Arrival_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arrival = "" + Arrival_Spinner.getItemAtPosition(i);
                arrival_latitude = latLngData.Arrival_latitude[uni][i];
                arrival_longitude = latLngData.Arrival_longitude[uni][i];
                mapFragment = MapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
                mapFragment.getMapAsync(MainActivity.this);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO
                for (int i=0; i< U_list.length; i++) {
                    if(U_list[i].equals(Uni)){
                        Uni_Spinner.setSelection(i);
                    }
                }
            }
        }, 500);

        inform_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this,InformChangeActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
            }
        });

        suggestion_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this,SuggestActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
            }
        });

        main_chat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                String departure = jsonResponse.getString("departure");
                                String arrival = jsonResponse.getString("arrival");
                                String user_1 = jsonResponse.getString("user_1");
                                String user_2 = jsonResponse.getString("user_2");
                                String user_3 = jsonResponse.getString("user_3");
                                String user_4 = jsonResponse.getString("user_4");
                                int quantity = jsonResponse.getInt("quantity");
                                Intent BoardCompleteIntent = new Intent(MainActivity.this,CompleteActivity.class);
                                BoardCompleteIntent.putExtra("departure",departure);
                                BoardCompleteIntent.putExtra("arrival",arrival);
                                BoardCompleteIntent.putExtra("userID",userID);
                                BoardCompleteIntent.putExtra("user_1",user_1);
                                BoardCompleteIntent.putExtra("user_2",user_2);
                                BoardCompleteIntent.putExtra("user_3",user_3);
                                BoardCompleteIntent.putExtra("user_4",user_4);
                                BoardCompleteIntent.putExtra("quantity",quantity);
                                BoardCompleteIntent.putExtra("isBoard",0);
                                BoardCompleteIntent.putExtra("U_list",U_list);
                                BoardCompleteIntent.putExtra("Uni",Uni);
                                startActivity(BoardCompleteIntent);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("참여한 퀵매치가 존재하지 않습니다.")
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
                QuickChatRequest quickChatRequest = new QuickChatRequest(userID,responseListener);
                quickchatQueue.add(quickChatRequest);
            }
        });

        navInfoImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x,y;
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) motionEvent.getRawX();
                        y = (int) motionEvent.getRawY();
                        Toast toast = Toast.makeText(MainActivity.this,"",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.LEFT,x,y);
                        TextView tvToastMsg = new TextView(MainActivity.this);
                        tvToastMsg.setText(" 다른유저가 사용자를 판단할수있는 신뢰도 점수입니다.\n\n"+
                                "*신뢰도 평가시*\n좋음 : +2점\n나쁨 : -3점");
                        tvToastMsg.setTextColor(Color.BLACK);
                        tvToastMsg.setTextSize(16);
                        tvToastMsg.setBackgroundColor(Color.WHITE);
                        toast.setView(tvToastMsg);
                        toast.show();
                        break;

                        default:
                        break;
                }
                return false;
            }
        });

        MainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float distance = 0;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressedX = motionEvent.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        distance = pressedX - motionEvent.getRawX();
                        break;
                    default:
                        break;
                }
                if(Math.abs(distance) > 100) {
                    if(distance > 0) {
                        dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.loading);
                        ImageView imageView = (ImageView)dialog.findViewById(R.id.loading_image);
                        imageView.setImageResource(R.drawable.loading);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        Intent home_boardIntent = new Intent(MainActivity.this, BoardActivity.class);
                        home_boardIntent.putExtra("userID",userID);
                        home_boardIntent.putExtra("trust",trust);
                        home_boardIntent.putExtra("Uni",Uni);
                        home_boardIntent.putExtra("U_list",U_list);
                        startActivity(home_boardIntent);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
                    }
                }
                return true;
            }
        });
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

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // ...
        if(uni !=0) {
            UiSettings uiSettings = naverMap.getUiSettings();
            Marker marker = new Marker();
            Marker marker2 = new Marker();
            LatLng latLng = new LatLng(departure_latitude, departure_longitude);
            LatLng latLng2 = new LatLng(arrival_latitude, arrival_longitude);

            InfoWindow infoWindow = new InfoWindow();
            infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
                @NonNull
                @Override
                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                    return "출발지";
                }
            });

            InfoWindow infoWindow2 = new InfoWindow();
            infoWindow2.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
                @NonNull
                @Override
                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                    return "도착지";
                }
            });

            uiSettings.setScaleBarEnabled(false);
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng);
            CameraUpdate cameraUpdate1 = CameraUpdate.zoomTo(15);
            naverMap.moveCamera(cameraUpdate);
            naverMap.moveCamera(cameraUpdate1);
            marker.setPosition(latLng);
            marker.setMap(naverMap);
            marker2.setPosition(latLng2);
            marker2.setMap(naverMap);
            infoWindow.open(marker);
            infoWindow2.open(marker2);

            PathOverlay path = new PathOverlay();
            path.setCoords(Arrays.asList(
                    latLng,
                    latLng2
            ));
            path.setOutlineWidth(5);
            path.setColor(Color.GREEN);
            path.setOutlineColor(Color.GREEN);
            path.setMap(naverMap);
        }
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }

    public void GuideIntent () {
        Intent GuideIntent = new Intent(MainActivity.this,GuideActivity.class);
        final int [] U = new int[2];
        final int [] D = new int[2];
        final int [] A = new int[2];
        final int [] M = new int[2];
        Uni_Spinner.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Uni_Spinner.getViewTreeObserver().removeOnPreDrawListener(this);
                //여기서 뷰의 크기를 가져온다.
                Uni_Spinner.getLocationOnScreen(U);
                return true;
            }
        });
        Uni_Spinner.getLocationOnScreen(U);
        Departure_Spinner.getLocationOnScreen(D);
        Arrival_Spinner.getLocationOnScreen(A);
        Match_Button.getLocationOnScreen(M);
        GuideIntent.putExtra("UX",U[0]);
        GuideIntent.putExtra("UY",U[1]);
        GuideIntent.putExtra("DX",Departure_Spinner.getX());
        GuideIntent.putExtra("DY",Departure_Spinner.getY());
        GuideIntent.putExtra("AX",Arrival_Spinner.getX());
        GuideIntent.putExtra("AY",Arrival_Spinner.getY());
        GuideIntent.putExtra("MX",Match_Button.getX() + Match_Button.getWidth()/2);
        GuideIntent.putExtra("MY",Match_Button.getY() + Match_Button.getHeight()/2);
        startActivity(GuideIntent);
    }


}