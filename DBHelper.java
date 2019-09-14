package com.unipool.unipool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context,"Unipool",null,VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append("Create Table login_info (");
        sb.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append("userID Text ,");
        sb.append("userPW Text ,");
        sb.append("uni Text )");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
