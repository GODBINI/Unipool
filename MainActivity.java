package com.unipool.unipool;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    String userID2;
    String trust;
    MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
    final StringList stringList = new StringList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        //변수 선언
        Intent beforeIntent = getIntent();
        final String userID = beforeIntent.getStringExtra("userID");
        final String Uni = beforeIntent.getStringExtra("Uni");
        userID2 = userID;
        final Button home_homeButton = (Button)findViewById(R.id.home_homeButton);
        final Button home_boardButton = (Button)findViewById(R.id.home_boardButton);
        final Button Match_Button = (Button)findViewById(R.id.Match_Button);
        final Button user_setting_Button = (Button)findViewById(R.id.user_setting_Button);
        final Spinner Uni_Spinner = (Spinner)findViewById(R.id.Uni_Spinner);
        final Spinner Departure_Spinner = (Spinner)findViewById(R.id.Departure_Spinner);
        final Spinner Arrival_Spinner = (Spinner)findViewById(R.id.Arrival_Spinner);
        final LatLngData latLngData = new LatLngData();

        ArrayAdapter UniSpinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stringList.U_list);

        // 네비게이션
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_headerView = navigationView.getHeaderView(0);
        final TextView nav_userID_Text = (TextView)nav_headerView.findViewById(R.id.nav_userID_Text);
        final TextView nav_userTrust_Text = (TextView)nav_headerView.findViewById(R.id.nav_userTrust_Text);

        DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                nav_userID_Text.setText("  안녕하세요,"+userID + "님");
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            trust = jsonResponse.getString("trust");
                            if(success) {
                                nav_userTrust_Text.setText("신뢰도 : " + trust + "점");
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                GetTrustRequest getTrustRequest = new GetTrustRequest(userID,responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(getTrustRequest);
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
        Uni_Spinner.setAdapter(UniSpinnerAdapter);

        user_setting_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        // 버튼클릭 리스너
        home_boardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.loading);
                ImageView imageView = (ImageView)dialog.findViewById(R.id.loading_image);
                imageView.setImageResource(R.drawable.loading);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Intent home_boardIntent = new Intent(MainActivity.this, BoardActivity.class);
                home_boardIntent.putExtra("userID",userID);
                home_boardIntent.putExtra("trust",trust);
                home_boardIntent.putExtra("Uni",Uni);
                startActivity(home_boardIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
            }
        });

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
                if(i==0)
                    select = false;
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

        for (int i=0; i< stringList.U_list.length; i++) {
            if(stringList.U_list[i].equals(Uni)){
                Uni_Spinner.setSelection(i);
            }
        }
    }
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-time>=1000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<1000){
            finish();
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
            Intent intent = new Intent(MainActivity.this,InformChangeActivity.class);
            intent.putExtra("userID",userID2);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this,SuggestActivity.class);
            intent.putExtra("userID",userID2);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
