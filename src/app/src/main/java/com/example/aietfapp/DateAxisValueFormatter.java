package com.example.aietfapp;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAxisValueFormatter extends ValueFormatter {
    private String period;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    public DateAxisValueFormatter(String period) {
        this.period = period;
        this.calendar = Calendar.getInstance();
        
        switch (period) {
            case "1D":
                dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                break;
            case "1W":
                dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
                break;
            case "1M":
                dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
                break;
            case "6M":
                dateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
                break;
            case "1Y":
                dateFormat = new SimpleDateFormat("MMM yy", Locale.getDefault());
                break;
            default:
                dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
                break;
        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;
        Date date = generateDateForIndex(index);
        return dateFormat.format(date);
    }

    private Date generateDateForIndex(int index) {
        calendar.setTime(new Date());
        
        switch (period) {
            case "1D":
                // 24시간 전부터 시작
                calendar.add(Calendar.HOUR_OF_DAY, -(24 - index));
                break;
            case "1W":
                // 7일 전부터 시작
                calendar.add(Calendar.DAY_OF_MONTH, -(7 - index));
                break;
            case "1M":
                // 30일 전부터 시작
                calendar.add(Calendar.DAY_OF_MONTH, -(30 - index));
                break;
            case "6M":
                // 6개월 전부터 시작 (주단위)
                calendar.add(Calendar.WEEK_OF_YEAR, -(26 - index));
                break;
            case "1Y":
                // 12개월 전부터 시작
                calendar.add(Calendar.MONTH, -(12 - index));
                break;
        }
        
        return calendar.getTime();
    }
}