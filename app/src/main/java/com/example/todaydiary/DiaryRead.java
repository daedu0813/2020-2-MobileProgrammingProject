package com.example.todaydiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.InputStream;

public class DiaryRead extends Activity {

    TextView textDate, diaryTitleRead, diaryTextRead;
    Button btnDelete;
    ImageView diaryImgViewRead;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_read);

        textDate = (TextView)findViewById(R.id.textDate);

        Intent mainIntent = getIntent();
        String IntentDateText = mainIntent.getStringExtra("valueDateText");
        textDate.setText(IntentDateText);

        diaryTitleRead = (TextView)findViewById(R.id.diaryTitleView);
        diaryTextRead = (TextView)findViewById(R.id.diaryTextView);
        diaryImgViewRead = (ImageView)findViewById(R.id.diaryImgView);

        try {
            InputStream inImg = openFileInput(IntentDateText + "-Img.txt");

            InputStream inTitle = openFileInput(IntentDateText + "-Title.txt");
            byte[] title = new byte[30];
            inTitle.read(title);
            diaryTitleRead.setText(title.toString());


            InputStream inText = openFileInput(IntentDateText + "-Text.txt");
        } catch (Exception e) {}
    }
}
