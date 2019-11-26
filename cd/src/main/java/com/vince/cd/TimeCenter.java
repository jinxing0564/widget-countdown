package com.vince.cd;

/**
 * Created by by vince on 2019/10/22.
 */
public class TimeCenter {
    private static TimeCenter instance;

    public static TimeCenter getInstance() {
        if(instance == null){
            instance = new TimeCenter();
        }
        return instance;
    }

    public long getTime() {
        return System.currentTimeMillis();
    }
}
