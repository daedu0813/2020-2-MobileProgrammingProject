package com.example.todaydiary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
    private ArrayList<DayInfo> arrayListDayInfo;
    public Date selectedDate;

    public CalendarAdapter(ArrayList<DayInfo> arrayLIstDayInfo, Date date) {
        this.arrayListDayInfo = arrayLIstDayInfo;
        this.selectedDate = date;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListDayInfo.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayListDayInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayInfo day = arrayListDayInfo.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day, parent, false);
        }

        if (day != null) {
            TextView tvDay = convertView.findViewById(R.id.day_cell_tv_day);
            tvDay.setText(day.getDay());

            ImageView ivSelected = convertView.findViewById(R.id.iv_selected);
            if (day.isSameDay(selectedDate)) {
                ivSelected.setVisibility(View.VISIBLE);
            } else {
                ivSelected.setVisibility(View.INVISIBLE);
            }

            if (day.isInMonth()) {
                if ((position % 7 + 1) == Calendar.SUNDAY) {
                    tvDay.setTextColor(Color.RED);
                } else if ((position % 7 + 1) == Calendar.SATURDAY) {
                    tvDay.setTextColor(Color.BLUE);
                } else {
                    tvDay.setTextColor(Color.BLACK);
                }
            } else {
                tvDay.setTextColor(Color.GRAY);
            }
        }
        convertView.setTag(day);

        return convertView;
    }

}