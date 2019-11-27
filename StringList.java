package com.unipool.unipool;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StringList {
    final public static int NUMBEROFSCHOOL = 7;
    final public static int NUMBEROFDEPARTURE = 3;
    final public static int NUMBEROFARRIVAL = 6;
    final public String[] U_list = new String[NUMBEROFSCHOOL];
    final public String[][] Departure_list = new String[NUMBEROFSCHOOL][NUMBEROFDEPARTURE];
    final public String[][] Arrival_list = new String[NUMBEROFSCHOOL][NUMBEROFARRIVAL];

    public StringList(Context context) {
        //학교
        U_list[0] = "대학교";      // 출발,도착지의 인덱스 연관
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");
                    int count = 0;
                    int id;
                    String university;
                    while (count < jsonArray.length()) {
                        JSONObject jsonObject = jsonArray.getJSONObject(count);
                        id = jsonObject.getInt("id");
                        university = jsonObject.getString("university");

                        U_list[id] = university;
                        count++;
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        UniversityRequest universityRequest = new UniversityRequest(responseListener);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(universityRequest);


        //출발지
        Departure_list[0][0] = "출발지";

        Departure_list[1][0] = "수원역 6번출구";
        Departure_list[1][1] = "수원역 롯데몰";
        Departure_list[1][2] = "병점역 2번출구";

        Departure_list[2][0] = "광교(경기대)역";

        Departure_list[3][0] = "광교중앙(아주대)역";

        Departure_list[4][0] = "병점역 1번출구";
        Departure_list[4][1] = "병점역 2번출구";

        Departure_list[5][0] = "수원역 6번출구";
        Departure_list[5][1] = "수원역 롯데몰";
        Departure_list[5][2] = "병점역 2번출구";

        Departure_list[6][0] = "성균관대역";
        //도착지
        Arrival_list[0][0] = "도착지";

        Arrival_list[1][0] = "수원대학교 정문";
        Arrival_list[1][1] = "종합강의동 사거리";
        Arrival_list[1][2] = "수원대학교 IT/생활과학대학";
        Arrival_list[1][3] = "수원대학교 종합강의동";
        Arrival_list[1][4] = "수원대학교 미래혁신관";
        Arrival_list[1][5] = "수원대학교 음악대학";

        Arrival_list[2][0] = "경기대학교 중앙도서관";

        Arrival_list[3][0] = "아주대학교 중앙도서관";

        Arrival_list[4][0] = "한신대학교 정문";

        Arrival_list[5][0] = "협성대학교 정문";

        Arrival_list[6][0] = "성균관대학교 학생회관";
        blank();

    }

    public void blank() {
         for(int i=1; i<NUMBEROFSCHOOL; i++) {
             for (int j = 0; j < NUMBEROFARRIVAL; j++) {
                 if (Arrival_list[i][j] == null) {
                     Arrival_list[i][j] = "";
                 }
             }
         }

        for(int i=1; i<NUMBEROFSCHOOL; i++) {
            for (int j = 0; j<NUMBEROFDEPARTURE; j++) {
                if(Departure_list[i][j] == null){
                    Departure_list[i][j] = "";
                }
            }
        }

    }
}
