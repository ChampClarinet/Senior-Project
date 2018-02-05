package com.easypetsthailand.champ.easypets.Core;

import android.util.Log;

import java.util.Calendar;

public class Utils {

    public static boolean[] openDaysStringToBooleanArray(String openDays) {
        boolean b[] = new boolean[8];
        for (int i = 0; i < openDays.length(); ++i) {
            b[i] = openDays.charAt(i) != '0';
        }
        return b;
    }

    /*public static double calculateDistance(double latitude, double longitude){

    }*/

    public static boolean isOpening(String openTime, String closeTime) {
        if(openTime != null && closeTime != null){
            Log.d("openTime", openTime);
            Log.d("closeTime", closeTime);
        }else{
            Log.d("openTime", "null");
            Log.d("closeTime", "null");
        }
        if (openTime == null || closeTime == null) return true;
        Calendar now = Calendar.getInstance();
        int hourOpen = Integer.parseInt(openTime.substring(0, 2));
        int minuteOpen = Integer.parseInt(openTime.substring(3, 5));
        int hourClose = Integer.parseInt(closeTime.substring(0, 2));
        int minuteClose = Integer.parseInt(closeTime.substring(3, 5));
        if (hourOpen < hourClose) return (now.get(Calendar.HOUR_OF_DAY) > hourOpen ||
                (now.get(Calendar.HOUR_OF_DAY) == hourOpen && now.get(Calendar.MINUTE) > minuteOpen)) && (now.get(Calendar.HOUR_OF_DAY) < hourClose ||
                now.get(Calendar.HOUR_OF_DAY) == hourClose && now.get(Calendar.MINUTE) < minuteClose);
        return (now.get(Calendar.HOUR_OF_DAY) < hourOpen ||
                (now.get(Calendar.HOUR_OF_DAY) == hourOpen && now.get(Calendar.MINUTE) > minuteOpen)) && (now.get(Calendar.HOUR_OF_DAY) < hourClose ||
                now.get(Calendar.HOUR_OF_DAY) == hourClose && now.get(Calendar.MINUTE) < minuteClose);
    }

}
