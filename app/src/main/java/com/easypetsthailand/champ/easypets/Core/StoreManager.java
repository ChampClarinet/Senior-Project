package com.easypetsthailand.champ.easypets.Core;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.R;

public class StoreManager {

    private static final String TAG = StoreManager.class.getSimpleName();
    private static final int timeOut = 3000;

    public static void like(Context context, int storeId, String userUid){
        final Boolean returnValue[] = {null};
        final String url = context.getString(R.string.URL) + context.getString(R.string.ATTEMPT_LIKE_URL, userUid, storeId);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("200")) returnValue[0] = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("attempt like error ", error.toString());
                Log.d("on request = ", url);
                returnValue[0] = false;
            }
        });
        Volley.newRequestQueue(context).add(request);
    }

    public static void bindReviewsCountToTextView(final Context context, int storeId, final TextView target){
        String url = context.getString(R.string.URL) + context.getString(R.string.REVIEWS_COUNT_URL, storeId);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int count = Integer.parseInt(response);
                    target.setText(context.getString(R.string.reviews, Integer.toString(count)));
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
        Volley.newRequestQueue(context).add(request);
    }

}
