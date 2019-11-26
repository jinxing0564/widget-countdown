package com.vince.countdown;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.vince.cd.BaseCountDownView;

/**
 * Created by vince on 2019-5-23.
 */
public class DemoCountDownView extends BaseCountDownView {
    private TextView tvTime;

    public DemoCountDownView(Context context) {
        super(context);
        init(context);
    }

    public DemoCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DemoCountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_demo_count_down, this);
        tvTime = (TextView) findViewById(R.id.tv_time);
    }

    @Override
    protected void onTimeCountTick(long millisUntilFinished) {
        updateTimeText(millisUntilFinished);
    }

    @Override
    protected void onTimeCountFinish() {
        tvTime.setText("倒计时结束");
    }

    private void updateTimeText(long millisUntilFinished) {
        long untilFinished = millisUntilFinished / 1000;
        int day = (int) (untilFinished / 86400);
        int hour = (int) ((untilFinished % 86400) / 3600);
        int minute = (int) ((untilFinished % 3600) / 60);
        int second = (int) (untilFinished % 60);
        String time;
        if (day > 0) {
            time = day + "天" + hour + "时" + minute + "分" + second + "秒";
        } else if (hour > 0) {
            time = hour + "时" + minute + "分" + second + "秒";
        } else if (minute > 0) {
            time = minute + "分" + second + "秒";
        } else {
            time = second + "秒";
        }
        tvTime.setText("倒计时" + time);

    }

}
