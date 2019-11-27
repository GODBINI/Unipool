package com.unipool.unipool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class GuideActivity extends AppCompatActivity {

    int image = 0;
    ImageView guide_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        guide_image = (ImageView)findViewById(R.id.guide_image);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                switch (image) {
                    case 0 :
                        guide_image.setImageResource(R.drawable.guide_2);
                        image++;
                        break;
                    case 1 :
                        guide_image.setImageResource(R.drawable.guide_3);
                        image++;
                        break;
                    case 2 :
                        guide_image.setImageResource(R.drawable.guide_3);
                        image++;
                        break;
                    case 3 :
                        guide_image.setImageResource(R.drawable.guide_4);
                        image++;
                        break;
                    case 4 :
                        guide_image.setImageResource(R.drawable.guide_5);
                        image++;
                        break;
                    case 5 :
                        guide_image.setImageResource(R.drawable.guide_6);
                        image++;
                        break;
                    case 6 :
                        AlertDialog.Builder builder = new AlertDialog.Builder(GuideActivity.this);
                        builder.setTitle("시작 가이드")
                                .setMessage("\n가이드가 종료되었습니다.\n유니풀 사용시 불편하신점은 건의 사항을 남겨주세요.\n\n감사합니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                        break;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void init() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuideActivity.this);
        builder.setTitle("시작 가이드")
                .setMessage("안녕하세요.\n유니풀을 처음 사용하는 분들을 위한 시작 가이드입니다.\n\n '확인'을 누르면 가이드를 시작합니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(GuideActivity.this,"화면을 터치하면 다음 가이드가 진행됩니다",Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
}
