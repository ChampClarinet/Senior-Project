package com.easypetsthailand.champ.easypets.Core;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.easypetsthailand.champ.easypets.Model.Service.Service;
import com.easypetsthailand.champ.easypets.R;

import java.util.Calendar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Utils {

    public static boolean[] openDaysStringToBooleanArray(String openDays) {
        boolean b[] = new boolean[7];
        for (int i = 0; i < openDays.length(); ++i) {
            b[i] = openDays.charAt(i) != '0';
        }
        return b;
    }

    public static double calculateDistance(double latitude, double longitude) {
        Location here = getCurrentLocation();
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return here.distanceTo(location);
    }

    public static Location getCurrentLocation() {
        GPSTracker tracker = GPSTracker.getInstance();
        Location here = new Location(LocationManager.GPS_PROVIDER);
        here.setLatitude(tracker.getLatitude());
        here.setLongitude(tracker.getLongitude());
        tracker.stopUsingGPS();
        return here;
    }

    public static String getGoogleMapsUri(double latitude, double longitude) {
        String uri = "https://www.google.com/maps/search/?api=1&query=";
        uri += latitude + "," + longitude;
        return uri;
    }

    private static AlertDialog dialog;

    public static AlertDialog createLoadDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.loading_layout, null);
        TextView tvLoading = layout.findViewById(R.id.loadingTextView);
        tvLoading.setText(message);
        builder.setView(layout);
        dialog = builder.create();
        Log.d("createdDialog", message);
        dialog.show();
        return dialog;
    }

    public static void dissmissLoadDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static boolean isOpening(String openDays, String openTime, String closeTime) {
        boolean[] b = openDaysStringToBooleanArray(openDays);
        return isOpening(b, openTime, closeTime);
    }

    public static boolean isOpening(boolean[] openDays, String openTime, String closeTime) {
        Calendar now = Calendar.getInstance();
        //check days
        int today = now.get(Calendar.DAY_OF_WEEK) - 1;
        if (!openDays[today]) return false;
        //check time
        if (openTime == null || closeTime == null) return true;
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

    public static void setToggleButtonBackground(Context context, CompoundButton button, boolean status) {
        if (status) {
            button.setBackgroundResource(R.drawable.toggle_button_toggled);
            button.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            button.setBackgroundResource(R.drawable.toggle_button);
            button.setTextColor(context.getResources().getColor(R.color.text));
        }
    }

    public static String getNextOpens(Service service, Context context) {
        String s = "";
        Calendar now = Calendar.getInstance();
        int today = now.get(Calendar.DAY_OF_WEEK) - 1;
        boolean[] openDays = service.getOpenDays();
        int nextOpen = -1;
        //open today but not open yet.
        if (openDays[today] && now.get(Calendar.HOUR_OF_DAY) < Integer.parseInt(service.getOpenTime().substring(0, 2))) {
            return s + context.getString(R.string.today) + " ";
        }
        //open on another day.
        for (int i = 1; i < openDays.length; i++) {
            int realIndex = (today + i) % openDays.length;
            boolean b = openDays[realIndex];
            if (b) {
                nextOpen = realIndex;
                break;
            }
        }
        //tomorrow
        if (Math.abs(nextOpen - today) == 1) {
            return s + context.getString(R.string.tomorrow) + " ";
        }
        String openDay = null;
        switch (nextOpen) {
            case 0:
                openDay = context.getString(R.string.SUN);
                break;
            case 1:
                openDay = context.getString(R.string.MON);
                break;
            case 2:
                openDay = context.getString(R.string.TUE);
                break;
            case 3:
                openDay = context.getString(R.string.WED);
                break;
            case 4:
                openDay = context.getString(R.string.THU);
                break;
            case 5:
                openDay = context.getString(R.string.FRI);
                break;
            case 6:
                openDay = context.getString(R.string.SAT);
                break;
        }
        s += openDay + " ";
        return s;
    }
}
