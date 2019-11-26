package com.vince.cd;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by vince on 2019-9-7.
 */

public class CountDownTimer {
    private long timeToCountDown = -1L;
    private long countDownInterval = -1L;
    private long beginTime;
    private CountDownTimerCallback callback;
    private int TICK_MSG = 1;
    private long lastTick = -1L;
    private boolean cancelled = false;

    public CountDownTimer(CountDownTimerCallback callback) {
        this.callback = callback;
    }

    public void setTime(long timeToCountDown, long countDownInterval) {
        this.timeToCountDown = timeToCountDown;
        this.countDownInterval = countDownInterval;
        if (callback != null) {
            this.beginTime = callback.getCurrentTimeMillis();
        }
    }

    public boolean isAvailable() {
        return callback != null && timeToCountDown != -1L && countDownInterval != -1L;
    }

    public void start() {
        cancel();
        if (callback == null) {
            throw new RuntimeException("callback must not be null");
        }
        checkTime();
        cancelled = false;
        Message msg = Message.obtain();
        msg.what = TICK_MSG;
        countHandler.handleMessage(msg); //首先同步回调回去一次，让上层立即更新ui
        countHandler.sendEmptyMessageDelayed(TICK_MSG, countDownInterval);
    }

    public void cancel() {
        cancelled = true;
        countHandler.removeMessages(TICK_MSG);
        lastTick = -1L;
    }

    public void clear() {
        cancel();
        timeToCountDown = -1L;
        countDownInterval = -1L;

    }

    private void checkTime() {
        if (timeToCountDown == -1L || countDownInterval == -1L) {
            throw new RuntimeException("timeToCountDown and countDownInterval should initialize");
        }
    }

    private long getTimeToCount() {
        return timeToCountDown - (callback.getCurrentTimeMillis() - beginTime);
    }

    private Handler countHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TICK_MSG) {
                if (cancelled) {
                    return;
                }
                long timeLeft = getTimeToCount();
                if (timeLeft <= 0) {
                    callback.onCountDownFinish();
                    countHandler.removeMessages(TICK_MSG);
                } else if (timeLeft <= countDownInterval) {
                    countHandler.sendEmptyMessageDelayed(TICK_MSG, timeLeft);
                } else {
                    callback.onTick(timeLeft);
                    if (lastTick < 0) {
                        lastTick = SystemClock.elapsedRealtime();
                    }
                    long delay = countDownInterval - (SystemClock.elapsedRealtime() - lastTick);
                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) {
                        delay += countDownInterval;
                    }
                    lastTick = SystemClock.elapsedRealtime();
                    countHandler.sendEmptyMessageDelayed(TICK_MSG, delay);
                }
            }
            super.handleMessage(msg);
        }
    };

    public interface CountDownTimerCallback {
        long getCurrentTimeMillis();

        void onTick(long millisUntilFinished);

        void onCountDownFinish();
    }
}
