package com.easypetsthailand.champ.easypets.Core;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.R;

import java.util.Calendar;

public class StoreManager {

    private static final String TAG = StoreManager.class.getSimpleName();
    private static final int timeOut = 3000;

    public static boolean checkLikeCondition(Context context, int store_id, String uid) {
        long l = Calendar.getInstance().getTimeInMillis();
        final Boolean[] condition = {null};
        final String url = context.getString(R.string.URL) + context.getString(R.string.LIKE_CONDITION_URL, store_id, uid);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")) {
                    condition[0] = true;
                } else if (response.equals("false")) {
                    condition[0] = false;
                }
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error\n"+url);
            }
        });
        Volley.newRequestQueue(context).add(request);
        while (true) {
            long l2 = Calendar.getInstance().getTimeInMillis();
            if (l2 - l > timeOut){
                Log.d(TAG, "check liked timed out");
                return false;
            }
            if(condition[0] != null) break;
        }
        Log.d(TAG, "like found, it's "+condition[0]);
        return condition[0];
    }

    public static int countLikeOf(Context context, int storeId) {
        final int[] likeCount = {-1};
        String url = context.getString(R.string.URL) + context.getString(R.string.LIKES_COUNT_URL, storeId);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    likeCount[0] = Integer.parseInt(response);
                } catch (NumberFormatException e) {

                }
                Log.d(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        long l = Calendar.getInstance().getTimeInMillis();
        Volley.newRequestQueue(context).add(request);
        while(likeCount[0] == -1){
            long l2 = Calendar.getInstance().getTimeInMillis();
            if(l2 - l > timeOut){
                Log.d(TAG, "likes count timed out");
                return 0;
            }
        }
        return likeCount[0];
    }

}
