package com.vince.cd;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by vince on 2019-9-8.
 */
public class CountDownTools {
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static long getStringTime(String timeString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
            Date date = format.parse(timeString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long calculateTimeToCount(String timeString) {
        long millisToCount;
        if (TextUtils.isEmpty(timeString)) {
            return 0;
        }
        millisToCount = getStringTime(timeString) - TimeCenter.getInstance().getTime();
        return millisToCount;
    }

    public static FormatTime getUnfinishTime(long millisUntilFinished) {
        FormatTime formatTime = new FormatTime();
        if (millisUntilFinished < 0) {
            return formatTime;
        }
        long untilFinished = millisUntilFinished / 1000;
        int hour = (int) (untilFinished / 3600);
        int minute = (int) ((untilFinished % 3600) / 60);
        int second = (int) (untilFinished % 60);
        formatTime.setHour(hour);
        formatTime.setMin(minute);
        formatTime.setSecond(second);
        return formatTime;
    }


    public static void findCountDownViews(View view, ArrayList<BaseCountDownView> viewList) {
        if (view == null || !(view instanceof ViewGroup)) {
            return;
        }
        if (view instanceof BaseCountDownView) {
            viewList.add((BaseCountDownView) view);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int len = viewGroup.getChildCount();
        for (int i = 0; i < len; i++) {
            View v = viewGroup.getChildAt(i);
            findCountDownViews(v, viewList);
        }
    }

    public static class FormatTime {
        private String hh;
        private String mm;
        private String ss;

        public FormatTime() {
            hh = "00";
            mm = "00";
            ss = "00";
        }

        public String getHour() {
            return hh;
        }

        public void setHour(int hour) {
            this.hh = getFormatedTime(hour);
        }

        public String getMin() {
            return mm;
        }

        public void setMin(int min) {
            this.mm = getFormatedTime(min);
        }

        public String getSecond() {
            return ss;
        }

        public void setSecond(int second) {
            this.ss = getFormatedTime(second);
        }

        private String getFormatedTime(int time) {
            String formatedTime = "";
            if (time < 10) {
                formatedTime = "0" + time;
            } else {
                formatedTime = "" + time;
            }
            return formatedTime;
        }
    }
}
