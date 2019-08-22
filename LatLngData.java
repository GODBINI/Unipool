package com.unipool.unipool;

import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class LatLngData {
    final public double[][] Departure_latitude = new double[4][2];
    final public double[][] Departure_longitude = new double[4][2];

    final public double[][] Arrival_latitude = new double[4][6];
    final public double[][] Arrival_longitude = new double[4][6];

    public LatLngData() {
        Departure_latitude[1][0] = 37.265194;         // 수원역 롯데몰
        Departure_longitude[1][0] = 126.997628;
        Departure_latitude[1][1] = 37.206549;         // 병점역 후문
        Departure_longitude[1][1] = 127.031998;
        Departure_latitude[2][0] = 37.300787;         // 경기대역
        Departure_longitude[2][0] = 127.044638;

        Arrival_latitude[1][0] = 37.214205;           // 수원대학교 정문
        Arrival_longitude[1][0] = 126.978817;
        Arrival_latitude[1][1] = 37.209502;           // 종합강의동 사거리
        Arrival_longitude[1][1] = 126.976996;
        Arrival_latitude[2][0] = 37.301127;           // 경기대학교 중앙도서관
        Arrival_longitude[2][0] = 127.035687;
    }
}
