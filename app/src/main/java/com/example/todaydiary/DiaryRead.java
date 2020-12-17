package com.example.todaydiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DiaryRead extends Activity {

    TextView textDate, diaryTitleRead, diaryTextRead;
    ImageButton btnModifyArtice, btnDeleteArticle;
    ImageView diaryImgViewRead;
    Switch playerSwitch;

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

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
        playerSwitch = (Switch)findViewById(R.id.playerSwitch);

        btnModifyArtice = (ImageButton) findViewById(R.id.btnModifyArticle);
        btnDeleteArticle = (ImageButton) findViewById(R.id.btnDeleteArticle);

        try {
            InputStream inTitle = openFileInput(IntentDateText + "-Title.txt");
            String title = null;
            byte[] titletxt = new byte[30];
            inTitle.read(titletxt);
            inTitle.close();
            title = (new String(titletxt)).trim();
            diaryTitleRead.setText(title);

            InputStream inText = openFileInput(IntentDateText + "-Text.txt");
            String text = null;
            byte[] texttxt = new byte[5000];
            inText.read(texttxt);
            inText.close();
            text = (new String(texttxt)).trim();
            diaryTextRead.setText(text);

            InputStream inImg = openFileInput(IntentDateText + "-Image.jpg");
            Bitmap img = BitmapFactory.decodeStream(inImg);
            inImg.close();
            diaryImgViewRead.setImageBitmap(img);

            MediaPlayer mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(getFilesDir() + "/" + IntentDateText + "-Audio.mp3");
            playerSwitch.setChecked(true);
            mp.prepare();
            mp.start();
            playerSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(playerSwitch.isChecked()==true) {
                        try {
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        mp.stop();
                }
            });
        } catch (Exception e) {}

        btnModifyArtice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modifyIntent = new Intent(getApplicationContext(), DiaryModify.class);
                modifyIntent.putExtra("IntentDateText", IntentDateText);
                modifyIntent.putExtra("originTitle", diaryTitleRead.getText().toString());
                modifyIntent.putExtra("originText", diaryTextRead.getText().toString());
                startActivity(modifyIntent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        btnDeleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File Ftitle = new File(getFilesDir() + "/" + IntentDateText + "-Title.txt");
                File Ftext = new File(getFilesDir() + "/" + IntentDateText + "-Text.txt");
                File Fimg = new File(getFilesDir() + "/" + IntentDateText + "-Image.jpg");
                File Fbgm = new File(getFilesDir() + "/" + IntentDateText + "-Audio.mp3");
                Ftitle.delete();
                Ftext.delete();
                Fimg.delete();
                Fbgm.delete();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
