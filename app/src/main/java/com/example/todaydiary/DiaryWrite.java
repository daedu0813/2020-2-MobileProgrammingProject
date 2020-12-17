package com.example.todaydiary;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;

public class DiaryWrite extends Activity {
    TextView textDate, diaryBgmViewWrite;
    EditText diaryTitleWrite, diaryTextWrite;
    Button btnFindIMG, btnFindBGM, btnSaveDiary;
    ImageView diaryImgViewWrite;

    Uri URI;

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write);

        textDate = (TextView)findViewById(R.id.textDate);
        diaryTitleWrite = (EditText)findViewById(R.id.diaryTitleWrite);
        diaryTextWrite = (EditText)findViewById(R.id.diaryTextWrite);
        btnFindBGM = (Button)findViewById(R.id.btnFindBGM);
        btnFindIMG = (Button)findViewById(R.id.btnFindIMG);
        btnSaveDiary = (Button)findViewById(R.id.btnSaveDiary);
        diaryImgViewWrite = (ImageView)findViewById(R.id.diaryImgViewWrite);
        diaryBgmViewWrite = (TextView)findViewById(R.id.diaryBgmViewWrite);

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

        btnFindBGM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bgmIntent = new Intent();
                bgmIntent.setType("audio/mpeg");
                bgmIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(bgmIntent, 1);
            }
        });
        btnSaveDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outImg = openFileOutput(IntentDateText + "-Image.jpg", 0);
                    Bitmap img = ((BitmapDrawable)diaryImgViewWrite.getDrawable()).getBitmap();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, outImg);
                    outImg.flush();
                    outImg.close();

                    FileOutputStream outTitle = openFileOutput(IntentDateText + "-Title.txt", Context.MODE_PRIVATE);
                    String title = diaryTitleWrite.getText().toString();
                    outTitle.write(title.getBytes());
                    outTitle.close();

                    FileOutputStream outText = openFileOutput(IntentDateText + "-Text.txt", Context.MODE_PRIVATE);
                    String text = diaryTextWrite.getText().toString();
                    outText.write(text.getBytes());
                    outText.close();

                    FileOutputStream outBgm = openFileOutput(IntentDateText + "-Audio.mp3", 0);
                    Log.d("URI", String.valueOf(URI));
                    InputStream inS = getContentResolver().openInputStream(URI);
                    byte[] bin = new byte[1024];
                    int len;
                    while((len=inS.read(bin))>0)
                        outBgm.write(bin, 0, len);
                    outBgm.close();
                    inS.close();

                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Toast.makeText(getApplicationContext(), IntentDateText + " 일기 저장 완료", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        } else if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();

                    URI = uri;

                    diaryBgmViewWrite.setText(getFileName(uri));
                } catch (Exception e) {}
            }
            else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "오디오 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
