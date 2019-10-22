package com.unipool.unipool;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity {
    Switch MapSwitch;
    Button OptionOkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        DBHelper dbHelper = new DBHelper(OptionActivity.this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        final String userID = getIntent().getStringExtra("userID");

        MapSwitch = (Switch)findViewById(R.id.MapSwitch);
        OptionOkButton = (Button)findViewById(R.id.OptionOk_Button);

        SQLiteDatabase db2 = dbHelper.getReadableDatabase();
        Cursor cursor = db2.rawQuery("select map from login_info",null);
        if(cursor.getCount() != 0) {
            int map = 1;
            while (cursor.moveToNext()) {
                map = cursor.getInt(0);
            }
            if(map==0) {
                MapSwitch.setChecked(false);
            }
            else {
                MapSwitch.setChecked(true);
            }
        }

        MapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    db.execSQL("update login_info set map = 1 where userID = ?",new String[]{userID});
                }
                else {
                    db.execSQL("update login_info set map = 0 where userID = ?",new String[]{userID});
                }
            }
        });

        OptionOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.close();
                finish();
            }
        });
    }
}
