package com.example.todaydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageButton btnWriteArtice, btnDeleteArticle;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("오늘의 일기");

        btnWriteArtice = (ImageButton) findViewById(R.id.btnWriteArticle);
        btnDeleteArticle = (ImageButton) findViewById(R.id.btnDeleteArticle);

        btnWriteArtice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, listener, 2020, 12, 24);
                dialog.show();
            }
        });

    }

    // DatePickerDialog
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear+1 + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DiaryWrite.class);
            intent.putExtra("valueDateText", year + "-" + monthOfYear+1 + "-" + dayOfMonth);
            startActivity(intent);
        }
    };
}
