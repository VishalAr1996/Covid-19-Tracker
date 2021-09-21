package com.example.covid_19tracker.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtil {

    public static String FormatDate(String date, int testCase) {
        Date mDate;
        String dateFormat;
        String timeFormat;
        try {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).parse(date);
            if (testCase == 0) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(mDate);
                return dateFormat;
            } else if (testCase == 1) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy").format(mDate);
                return dateFormat;
            } else if (testCase == 2) {
                timeFormat = new SimpleDateFormat("hh:mm:ss a").format(mDate);
                return timeFormat;
            } else {
                return "Error";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
}
