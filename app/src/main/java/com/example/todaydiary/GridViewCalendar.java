package com.example.todaydiary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GridViewCalendar extends DialogFragment {

    private TextView tvCalendarTitle;
    private TextView tvSelectedDate;
    private GridView gvCalendar;

    private ArrayList<DayInfo> arrayListDayInfo;
    Calendar mThisMonthCalendar;
    CalendarAdapter mCalendarAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(EEE)", Locale.getDefault());
    Date selectedDate;

    public void setSelectedDate(Date date){
        selectedDate = date;

        if(mCalendarAdapter != null) {
            mCalendarAdapter.selectedDate = date;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.choice_date, null, false);

        Button btnPreviousCalendar = dialog.findViewById(R.id.btn_previous_calendar);
        Button btnNextCalendar = dialog.findViewById(R.id.btn_next_calendar);

        tvCalendarTitle = dialog.findViewById(R.id.tv_calendar_title);
        tvSelectedDate = dialog.findViewById(R.id.tv_selected_date);
        gvCalendar = dialog.findViewById(R.id.gv_calendar);

        btnPreviousCalendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.MONTH, -1);

                getCalendar(mThisMonthCalendar.getTime());
            }
        });
        btnNextCalendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mThisMonthCalendar.add(Calendar.MONTH, +1);

                getCalendar(mThisMonthCalendar.getTime());
            }
        });

        gvCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectedDate(((DayInfo)view.getTag()).getDate());

                tvSelectedDate.setText(sdf.format(mCalendarAdapter.selectedDate));

                mCalendarAdapter.notifyDataSetChanged();
            }
        });

        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                GridViewCalendar.this.getDialog().cancel();
                //((MainActivity)getActivity()).selectedDate = selectedDate;

                //((MainActivity)getActivity()).tvSelectedDate.setText(sdf.format(selectedDate));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                GridViewCalendar.this.getDialog().cancel();
            }
        });

        arrayListDayInfo = new ArrayList<>();

        builder.setView(dialog);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        mThisMonthCalendar = Calendar.getInstance();
        getCalendar(mThisMonthCalendar.getTime());
    }

    private void getCalendar(Date dateForCurrentMonth){
        int dayOfWeek;
        int thisMonthLastDay;

        arrayListDayInfo.clear();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateForCurrentMonth);

        calendar.set(Calendar.DATE, 1);//1일로 변경
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//1일의 요일 구하기
        Log.d("CalendarTest", "dayOfWeek = " + dayOfWeek+"");

        if(dayOfWeek == Calendar.SUNDAY){ //현재 달의 1일이 무슨 요일인지 검사
            dayOfWeek += 7;
        }

        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        setCalendarTitle();

        DayInfo day;

        calendar.add(Calendar.DATE, -1*(dayOfWeek-1)); //현재 달력화면에서 보이는 지난달의 시작일
        for(int i=0; i<dayOfWeek-1; i++){
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        for(int i=1; i <= thisMonthLastDay; i++){
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(true);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        for(int i=1; i<42-(thisMonthLastDay+dayOfWeek-1)+1; i++) {
            day = new DayInfo();
            day.setDate(calendar.getTime());
            day.setInMonth(false);
            arrayListDayInfo.add(day);

            calendar.add(Calendar.DATE, +1);
        }

        mCalendarAdapter = new CalendarAdapter(arrayListDayInfo, selectedDate);
        gvCalendar.setAdapter(mCalendarAdapter);

        tvSelectedDate.setText(sdf.format(selectedDate));
    }

    private void setCalendarTitle(){
        StringBuilder sb = new StringBuilder();

        sb.append(mThisMonthCalendar.get(Calendar.YEAR))
                .append("년 ")
                .append((mThisMonthCalendar.get(Calendar.MONTH) + 1))
                .append("월");
        tvCalendarTitle.setText(sb.toString());
    }
}