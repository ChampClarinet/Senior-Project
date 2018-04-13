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

import com.easypetsthailand.champ.easypets.R;

import java.util.Calendar;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Utils {

    public static boolean[] openDaysStringToBooleanArray(String openDays) {
        boolean b[] = new boolean[8];
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

    public static boolean isOpening(String openTime, String closeTime) {
        /*if (openTime != null && closeTime != null) {
            Log.d("openTime", openTime);
            Log.d("closeTime", closeTime);
        } else {
            Log.d("openTime", "null");
            Log.d("closeTime", "null");
        }*/
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

    public static void setToggleButtonBackground(Context context, CompoundButton button, boolean status) {
        if (status) {
            button.setBackgroundResource(R.drawable.toggle_button_toggled);
            button.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else {
            button.setBackgroundResource(R.drawable.toggle_button);
            button.setTextColor(context.getResources().getColor(R.color.text));
        }
    }

}
