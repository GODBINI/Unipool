package com.unipool.unipool;

public class StringList {
    final public String[] U_list = new String[4];
    final public String[][] Departure_list = new String[4][2];
    final public String[][] Arrival_list = new String[4][6];

    public StringList() {
        //학교
        U_list[0] = "대학교";
        U_list[1] = "수원대학교";                          // 출발,도착지의 인덱스 연관
        U_list[2] = "경기대학교";
        U_list[3] = "아주대학교";
        //출발지
        Departure_list[0][0] = "출발지";
        Departure_list[0][1] = "";

        Departure_list[1][0] = "수원역 롯데몰";
        Departure_list[1][1] = "병점역 후문";

        Departure_list[2][0] = "경기대역";
        Departure_list[2][1] = "";

        Departure_list[3][0] = "아주대역";
        Departure_list[3][1] = "";
        //도착지
        Arrival_list[0][0] = "도착지";

        Arrival_list[1][0] = "수원대학교 정문";
        Arrival_list[1][1] = "종합강의동 사거리";
        Arrival_list[1][2] = "수원대학교 IT대학";
        Arrival_list[1][3] = "수원대학교 종합강의동";
        Arrival_list[1][4] = "수원대학교 미래혁신관";
        Arrival_list[1][5] = "수원대학교 음악대학";

        Arrival_list[2][0] = "경기대학교 중앙도서관";

        Arrival_list[3][0] = "아주대학교 정문";
        blank();

    }

    public void blank() {
         for(int i=1; i<6; i++) {
            Arrival_list[0][i] = "";
            Arrival_list[2][i] = "";
            Arrival_list[3][i] = "";
        }

    }
}
