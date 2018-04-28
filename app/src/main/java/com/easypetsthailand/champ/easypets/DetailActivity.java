package com.easypetsthailand.champ.easypets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.Adapters.OtherServiceAdapter;
import com.easypetsthailand.champ.easypets.Model.OtherService;
import com.easypetsthailand.champ.easypets.Model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private final String TAG = DetailActivity.class.getSimpleName();
    private Service service;
    private ArrayList<OtherService> otherServiceArrayList = new ArrayList<>();
    private OtherServiceAdapter otherServiceAdapter;

    private void setGroom(){
        String url = getString(R.string.URL) + getString(R.string.GET_GROOM_BY_SERVICE_ID_URL, Integer.toString(service.getServiceId()));
        Log.d("querying groom", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    Log.d("groom response", "empty result");
                    groomCard.setVisibility(View.GONE);
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    int groomPrice = object.getInt("grooming_price_rate");
                    String price = "200";
                    switch (groomPrice){
                        case 1: price = "200";
                        break;
                        case 2: price = "300";
                        break;
                        case 3: price = "500";
                        break;
                        case 4: price = "800";
                        break;
                        case 5: price = "1000";
                        break;
                    }
                    groomStartPriceTextView.setText(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("json_error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("groom", "get groom error: " + error);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void setHospital(){
        String url = getString(R.string.URL) + getString(R.string.GET_HOSPITAL_BY_SERVICE_ID_URL, Integer.toString(service.getServiceId()));
        Log.d("querying hospital", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    Log.d("hospital response", "empty result");
                    hospitalCard.setVisibility(View.GONE);
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    int isAcceptBigOperation = object.getInt("is_accept_big_operation");
                    int checkupPriceRate = object.getInt("checkup_price_rate");
                    int vaccinePriceRate = object.getInt("vaccine_price_rate");
                    int operationPriceRate = object.getInt("operation_price_rate");
                    if(isAcceptBigOperation==0){
                        String x = getString(R.string.prefix_not)+getString(R.string.operation_acceptance);
                        isAcceptBigOperationTextView.setText(x);
                    }
                    else isAcceptBigOperationTextView.setText(getString(R.string.operation_acceptance));
                    String price = "50";
                    switch (checkupPriceRate){
                        case 1: price = "50";
                        break;
                        case 2: price = "100";
                        break;
                        case 3: price = "200";
                        break;
                        case 4: price = "500";
                        break;
                    }
                    checkupStartPrice.setText(price);
                    switch (vaccinePriceRate){
                        case 1: price = "50";
                        break;
                        case 2: price = "100";
                        break;
                        case 3: price = "200";
                        break;
                        case 4: price = "500";
                        break;
                    }
                    vaccineStartPrice.setText(price);
                    switch (operationPriceRate){
                        case 1: price = "100";
                        break;
                        case 2: price = "300";
                        break;
                        case 3: price = "500";
                        break;
                    }
                    operationStartPrice.setText(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("json_error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hospital", "get hospital error: " + error);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void setHotel(){
        String url = getString(R.string.URL) + getString(R.string.GET_HOTEL_BY_SERVICE_ID_URL, Integer.toString(service.getServiceId()));
        Log.d("querying hotel", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    Log.d("hotel response", "empty result");
                    hotelCard.setVisibility(View.GONE);
                    return;
                }
                try {
                    JSONObject object = new JSONObject(response);
                    int isAcceptOvernight = object.getInt("is_accept_overnight");
                    int hotelPrice = object.getInt("hotel_price");
                    if(isAcceptOvernight==0) {
                        String x = getString(R.string.prefix_not)+getString(R.string.overnight_acceptance);
                        isAcceptOvernightTextView.setText(x);
                    }
                    else isAcceptOvernightTextView.setText(getString(R.string.overnight_acceptance));
                    String price = "100";
                    switch (hotelPrice){
                        case 1: price = "100";
                        break;
                        case 2: price = "200";
                        break;
                        case 3: price = "300";
                        break;
                        case 4: price = "500";
                        break;
                        case 5: price = "1000";
                        break;
                    }
                    checkupStartPrice.setText(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("json_error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hotel", "get hotel error: " + error);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void setOther(){
        String url = getString(R.string.URL) + getString(R.string.GET_OTHERS_BY_SERVICE_ID_URL, Integer.toString(service.getServiceId()));
        Log.d("querying other", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    Log.d("other response", "empty result");
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i=0;i<array.length();++i){
                        JSONObject object = array.getJSONObject(i);
                        String details = object.getString("service_details");
                        String price = object.getString("service_price");
                        otherServiceArrayList.add(new OtherService(details, price));
                        otherServiceAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("json_error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("other", "get other error: " + error);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        service = (Service) getIntent().getSerializableExtra(getString(R.string.model_name_service));
        Log.d(TAG + ": is service null", String.valueOf(service == null));

        ButterKnife.bind(this);
        getBackIcon();

        otherServiceAdapter = new OtherServiceAdapter(otherServiceArrayList, this);
        otherServiceRV.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        otherServiceRV.setAdapter(otherServiceAdapter);

        setGroom();
        setHospital();
        setHotel();
        setOther();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBackIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @BindView(R.id.groom_start_price)
    TextView groomStartPriceTextView;
    @BindView(R.id.is_accept_big_operation)
    TextView isAcceptBigOperationTextView;
    @BindView(R.id.checkup_start_price)
    TextView checkupStartPrice;
    @BindView(R.id.vaccine_start_price)
    TextView vaccineStartPrice;
    @BindView(R.id.operation_start_price)
    TextView operationStartPrice;
    @BindView(R.id.is_accept_overnight)
    TextView isAcceptOvernightTextView;
    @BindView(R.id.hotel_start_price)
    TextView hotelStartPriceTextView;
    @BindView(R.id.rv_other_service)
    RecyclerView otherServiceRV;
    @BindView(R.id.card_groom)
    CardView groomCard;
    @BindView(R.id.card_hospital)
    CardView hospitalCard;
    @BindView(R.id.card_hotel)
    CardView hotelCard;


}
