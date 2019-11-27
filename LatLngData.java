package com.unipool.unipool;

import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class LatLngData {
    final public static int NUMBEROFSCHOOL = 7;
    final public static int NUMBEROFDEPARTURE = 3;
    final public static int NUMBEROFARRIVAL = 6;
    final public double[][] Departure_latitude = new double[NUMBEROFSCHOOL][NUMBEROFDEPARTURE];
    final public double[][] Departure_longitude = new double[NUMBEROFSCHOOL][NUMBEROFDEPARTURE];

    final public double[][] Arrival_latitude = new double[NUMBEROFSCHOOL][NUMBEROFARRIVAL];
    final public double[][] Arrival_longitude = new double[NUMBEROFSCHOOL][NUMBEROFARRIVAL];

    public LatLngData() {
        Departure_latitude[1][0] = 37.266043;         // 수원역 6번출구
        Departure_longitude[1][0] = 127.000782;
        Departure_latitude[1][1] = 37.265194;         // 수원역 롯데몰
        Departure_longitude[1][1] = 126.997628;
        Departure_latitude[1][2] = 37.206549;         // 병점역 후문(2번출구)
        Departure_longitude[1][2] = 127.031998;

        Departure_latitude[2][0] = 37.300787;         // 경기대역
        Departure_longitude[2][0] = 127.044638;

        Departure_latitude[3][0] = 37.288513;         // 아주대역
        Departure_longitude[3][0] = 127.051640;

        Departure_latitude[4][0] = 37.207078;         // 병점역 1번출구
        Departure_longitude[4][0] = 127.033671;
        Departure_latitude[4][1] = 37.206549;         // 병점역 후문(2번출구)
        Departure_longitude[4][1] = 127.031998;

        Departure_latitude[5][0] = 37.266043;         // 수원역 6번출구
        Departure_longitude[5][0] = 127.000782;
        Departure_latitude[5][1] = 37.265194;         // 수원역 롯데몰
        Departure_longitude[5][1] = 126.997628;
        Departure_latitude[5][2] = 37.206549;         // 병점역 후문(2번출구)
        Departure_longitude[5][2] = 127.031998;

        Departure_latitude[6][0] = 37.300265;         // 성균관대역
        Departure_longitude[6][0] = 126.970667;

        Arrival_latitude[1][0] = 37.214205;           // 수원대학교 정문
        Arrival_longitude[1][0] = 126.978817;
        Arrival_latitude[1][1] = 37.209502;           // 종합강의동 사거리
        Arrival_longitude[1][1] = 126.976996;
        Arrival_latitude[1][2] = 37.209505;           // 수원대학교 IT/생활과학
        Arrival_longitude[1][2] = 126.975179;
        Arrival_latitude[1][3] = 37.209053;           // 수원대학교 종합강의동
        Arrival_longitude[1][3] = 126.978388;

        Arrival_latitude[2][0] = 37.301127;           // 경기대학교 중앙도서관
        Arrival_longitude[2][0] = 127.035687;

        Arrival_latitude[3][0] = 37.281802;           // 아주대학교 중앙도서관
        Arrival_longitude[3][0] = 127.043532;

        Arrival_latitude[4][0] = 37.194303;           // 한신대학교 정문
        Arrival_longitude[4][0] = 127.023077;

        Arrival_latitude[5][0] = 37.215051;           // 협성대학교 정문
        Arrival_longitude[5][0] = 126.952498;

        Arrival_latitude[6][0] = 37.294024;           // 성균관대 학생회관
        Arrival_longitude[6][0] = 126.973247;

    }
}
