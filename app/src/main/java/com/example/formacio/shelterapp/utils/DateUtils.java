package com.example.formacio.shelterapp.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getFormattedTime(int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(c.getTime());
    }

    public static String getFormattedTime (long time){
        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date(time));
    }

    public static long dateToUnixTime (int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        return c.getTimeInMillis();
    }
}
