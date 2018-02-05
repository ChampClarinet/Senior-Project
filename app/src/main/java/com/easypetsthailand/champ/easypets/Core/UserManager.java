package com.easypetsthailand.champ.easypets.Core;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static final String TAG = UserManager.class.getSimpleName();

    private static void importUser(final Context context, final FirebaseUser user) {
        Log.d("import", "importing " + user.getDisplayName());
        StrictMode.enableDefaults();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = context.getString(R.string.URL) + context.getString(R.string.ADD_NEW_USER_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "reached", Toast.LENGTH_LONG).show();
                Log.d(
                        "status code is ", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "unreached", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", user.getUid());
                params.put("name", user.getDisplayName());
                params.put("email", user.getEmail());
                params.put("picturePath", user.getPhotoUrl().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public static void importUserProfile(final Context context, final FirebaseUser user) {
        String url = context.getString(R.string.URL) + context.getString(R.string.IS_USER_EXISTS_URL, user.getUid());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("requestResults", response);
                if (response.equals("404")) {
                    importUser(context, user);
                }else{
                    Log.d("lookup", "already");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("lookup", "lookup failed");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

}
