package com.example.todaydiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.BitSet;

public class DiaryWrite extends Activity {

    TextView textDate;
    EditText diaryTitleWrite, diaryTextWrite;
    Button btnFindIMG, btnFindBGM, btnSaveDiary;
    ImageView diaryImgViewWrite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write);
        setTitle("일기 쓰기");

        textDate = (TextView)findViewById(R.id.textDate);
        diaryTitleWrite = (EditText)findViewById(R.id.diaryTitleWrite);
        diaryTextWrite = (EditText)findViewById(R.id.diaryTextWrite);
        btnFindBGM = (Button)findViewById(R.id.btnFindBGM);
        btnFindIMG = (Button)findViewById(R.id.btnFindIMG);
        btnSaveDiary = (Button)findViewById(R.id.btnSaveDiary);
        diaryImgViewWrite = (ImageView)findViewById(R.id.diaryImgViewWrite);

        Intent mainIntent = getIntent();
        String IntentDateText = mainIntent.getStringExtra("valueDateText");
        textDate.setText(IntentDateText);

        btnFindIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(Intent.ACTION_GET_CONTENT);
                imgIntent.setType("image/*");
                imgIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(imgIntent, 0);
            }
        });

        btnSaveDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(IntentDateText + "-Img.jpg");
                    FileOutputStream outImg = openFileOutput(IntentDateText + "-Img.jpg", 0);
                    Bitmap img = ((BitmapDrawable)diaryImgViewWrite.getDrawable()).getBitmap();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, outImg);
                    outImg.flush();
                    outImg.close();
                } catch (Exception e) {}
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
            if(resultCode == RESULT_OK) {
                try {
                    InputStream inImg = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(inImg);
                    inImg.close();

                    diaryImgViewWrite.setImageBitmap(img);
                } catch (Exception e) {}
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}
