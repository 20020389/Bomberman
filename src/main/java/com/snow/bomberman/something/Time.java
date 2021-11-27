package com.snow.bomberman.something;

import com.snow.bomberman.PlayWindow;

import java.time.Duration;
import java.time.LocalDateTime;

public class Time {
    public static long LIMITTIME = 200;
    private static LocalDateTime TIMESTART = LocalDateTime.now();
    private static LocalDateTime TIMENOW;
    private static long time = LIMITTIME;
    private static long timenow = LIMITTIME;


    public static void resetTime() {
        timenow = LIMITTIME;
        TIMESTART = LocalDateTime.now();
    }

    public static void clock() {
        if (!PlayWindow.PAUSE) {
            TIMENOW = LocalDateTime.now();
            long range = Duration.between(TIMESTART, TIMENOW).getSeconds();
            time = timenow - range;
//            System.out.println(timenow);
            return;
        }
        TIMESTART = LocalDateTime.now();
        timenow = time;
//        System.out.println(timenow);
    }

    public static long getTime() {
        return time;
    }


}
