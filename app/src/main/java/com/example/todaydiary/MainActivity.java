package com.example.todaydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.security.Signature;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageButton btnWriteArtice, btnDeleteArticle;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("오늘의 일기");

        btnWriteArtice = (ImageButton) findViewById(R.id.btnWriteArticle);
        //btnDeleteArticle = (ImageButton) findViewById(R.id.btnDeleteArticle);

        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        btnWriteArtice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, listener, currentYear, currentMonth, currentDay);
                dialog.show();
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        ArrayList<String> diaryList = new ArrayList<String>();

        File[] listFiles = new File(String.valueOf(getFilesDir())).listFiles();
        String fileName, extName, finalName;
        for(File file : listFiles) {
            fileName = file.getName();
            Log.d("fName", fileName);
            extName = fileName.substring(fileName.length()-5);
            Log.d("eName", extName);
            finalName = fileName.substring(0, fileName.length()-10);
            Log.d("finalName", finalName);
            if(extName.equals("o.mp3"))
                continue;
            else if (extName.equals("e.jpg"))
                continue;
            else if (extName.equals("t.txt"))
                continue;
            else {
                diaryList.add(finalName);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, diaryList);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        Collections.sort(diaryList, myComparator);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DiaryRead.class);
                intent.putExtra("valueDateText", String.valueOf(diaryList.get(position)));
                startActivity(intent);
            }
        });
//        adapter.notifyDataSetChanged();
//        listView.invalidateViews();
    }

    // DatePickerDialog
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String month = null;
            String day = null;
            if(monthOfYear < 10) month = "0" + monthOfYear;
            else month = "" + monthOfYear;
            if(dayOfMonth < 10) day = "0" + dayOfMonth;
            else day = "" + dayOfMonth;
            Toast.makeText(getApplicationContext(), year + "년 " + month + "월 " + day + "일 일기 작성", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DiaryWrite.class);
            intent.putExtra("valueDateText", year + "-" + month + "-" + day);
            startActivity(intent);
        }
    };

    private final static Comparator myComparator = new Comparator() {
        private final Collator collator= Collator.getInstance();
        @Override
        public int compare(Object o1, Object o2) {
            return collator.compare(o1, o2);
        }
    };
}
