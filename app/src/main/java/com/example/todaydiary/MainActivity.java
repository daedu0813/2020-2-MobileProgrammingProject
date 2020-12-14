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

import java.io.File;
import java.security.Signature;
import java.util.ArrayList;
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
        btnDeleteArticle = (ImageButton) findViewById(R.id.btnDeleteArticle);

        btnWriteArtice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, listener, 2020, 12, 24);
                dialog.show();
            }
        });

        listView = (ListView)findViewById(R.id.listView);

        String [] files = null;
        String fileName = String.valueOf(getFilesDir());
        File file = new File(fileName);
        files = file.list();

        int index = 0;

        for(String fName : files) {
            files[index] = files[index].substring(0, 10);
            Log.d("LOG", String.valueOf(fName));
            index++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, files);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        String[] finalFiles = files;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DiaryRead.class);
                intent.putExtra("valueDateText", finalFiles);
                startActivity(intent);
            }
        });


//        btnDeleteArticle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int count, checked ;
//                count = adapter.getCount() ;
//
//                if (count > 0) {
//                    // 현재 선택된 아이템의 position 획득.
//                    checked = listView.getCheckedItemPosition();
//
//                    if (checked > -1 && checked < count) {
//                        // 아이템 삭제
//                        files.remove(checked) ;
//
//                        // listview 선택 초기화.
//                        listView.clearChoices();
//
//                        // listview 갱신.
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
    }

    // DatePickerDialog
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DiaryWrite.class);
            intent.putExtra("valueDateText", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            startActivity(intent);
        }
    };
}
