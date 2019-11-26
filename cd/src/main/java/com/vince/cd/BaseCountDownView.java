package com.vince.cd;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by vince on 2019-9-8.
 */

public abstract class BaseCountDownView extends LinearLayout {
    private CountDownTimer countDownTimer;
    public CountDownListener mCountDownListener;
    private boolean isAttachWindow = false;
    public static final int STATUS_COUNTING = 1;
    public static final int STATUS_END = 2;
    private int mStatus = STATUS_COUNTING;

    private CountDownTimer.CountDownTimerCallback callback = new CountDownTimer.CountDownTimerCallback() {
        @Override
        public long getCurrentTimeMillis() {
            return TimeCenter.getInstance().getTime();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            onTimeCountTick(millisUntilFinished);
            notifyCountDownTick(millisUntilFinished);
        }

        @Override
        public void onCountDownFinish() {
            mStatus = STATUS_END;
            onTimeCountFinish();
            notifyCountDownFinish();
        }
    };

    public BaseCountDownView(Context context) {
        super(context);
        init();
    }

    public BaseCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        countDownTimer = new CountDownTimer(callback);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

//        ArrayList<HDBaseCountDownView> viewList = new ArrayList<>();
//        CountDownTools.findCountDownViews(changedView, viewList);
//        for (HDBaseCountDownView countDownView : viewList) {
//            if (visibility == VISIBLE) {
//                countDownView.startCountDown();
//            } else {
//                countDownView.cancelCountDown();
//            }
//        }

        //手动设置该countdown view是否显示的时候也会回调此方法，这个时候changeView == 当前view
        //其他将页面后台隐藏的情况回调时changeView == 当前view的父view
        if (changedView instanceof BaseCountDownView) {
            return;
        }
        if (visibility == VISIBLE && ViewUtils.isMiddleViewVisible(this, changedView)) {
            startCountDown();
        } else {
            cancelCountDown();
        }
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        startCountDown();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        cancelCountDown();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachWindow = true;
        startCountDown();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachWindow = false;
        cancelCountDown();
    }

    public void setCountDownListener(CountDownListener listener) {
        this.mCountDownListener = listener;
    }

    protected void setTime(long timeToCountDown, long countDownInterval) {
        countDownTimer.setTime(timeToCountDown, countDownInterval);
    }

    public void cleanTimer() {
        countDownTimer.clear();
    }

    protected CountDownTools.FormatTime getUnfinishTime(long millis) {
        return CountDownTools.getUnfinishTime(millis);
    }

    /**
     * 此方法用于更新控件状态，并且进行倒计时的操作。
     *
     * @param timeToCountDown   设置需要的倒计时时间
     * @param countDownInterval 倒计时间隔
     */
    public void startCountDown(long timeToCountDown, long countDownInterval) {
        setTime(timeToCountDown, countDownInterval);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mStatus = STATUS_COUNTING;
                startCountDown();
            }
        });
    }

    private void startCountDown() {
        if (mStatus != STATUS_COUNTING) {
            return;
        }
        if (!isAttachWindow) {
            return;
        }
        if (countDownTimer.isAvailable()) { //此判断一般为true
            countDownTimer.start();
        } else {
            notifyCountDownFinish();
        }
    }

    public void cancelCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    protected void notifyCountDownFinish() {
        if (mCountDownListener != null) {
            mCountDownListener.onFinish();
        }
    }

    protected void notifyCountDownTick(long millisUntilFinished) {
        if (mCountDownListener != null) {
            mCountDownListener.onTick(millisUntilFinished);
        }
    }

    protected abstract void onTimeCountTick(long millisUntilFinished);

    protected abstract void onTimeCountFinish();

    public interface CountDownListener {
        void onTick(long millisUntilFinished);

        void onEvent(int event);

        void onFinish();
    }
}
