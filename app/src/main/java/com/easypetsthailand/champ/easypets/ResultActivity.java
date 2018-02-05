package com.easypetsthailand.champ.easypets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.Adapters.ResultAdapter;
import com.easypetsthailand.champ.easypets.Model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.result_label_zero_results)
    TextView zeroResultsLabel;
    @BindView(R.id.result_rv)
    RecyclerView resultRecyclerView;

    private ArrayList<Store> stores = new ArrayList<>();
    private ResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.resultActivity_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        String text = getIntent().getStringExtra("filter_text");
        String type = getIntent().getStringExtra("filter_type");

        query(text, type);

        adapter = new ResultAdapter(stores, this);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultRecyclerView.setHasFixedSize(true);
        resultRecyclerView.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void query(String query, String type) {
        String url = getString(R.string.URL) + getString(R.string.GET_STORE_URL, query, type);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("404")){
                    Log.d("response", "empty result");
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    stores.clear();
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject object = array.getJSONObject(i);
                        int storeId = object.getInt(getString(R.string.store_id));
                        String name = object.getString(getString(R.string.name));
                        String logoPath = object.getString(getString(R.string.logo_path));
                        String picturePath = object.getString(getString(R.string.picture_path));
                        String type = object.getString(getString(R.string.type));
                        String openDays = object.getString(getString(R.string.open_days));
                        String openTime = object.getString(getString(R.string.open_time));
                        if (openTime.equals("null")) openTime = null;
                        else openTime = openTime.substring(0, openTime.length() - 3);
                        String closeTime = object.getString(getString(R.string.close_time));
                        if (closeTime.equals("null")) closeTime = null;
                        else closeTime = closeTime.substring(0, closeTime.length() - 3);
                        String tel = object.getString(getString(R.string.tel));
                        int priceRate = object.getInt(getString(R.string.price_rate));
                        int likes = object.getInt(getString(R.string.likes_count));
                        double latitude = object.getDouble(getString(R.string.latitude));
                        double longitude = object.getDouble(getString(R.string.longitude));
                        String description = object.getString(getString(R.string.description));

                        Store newStore = new Store(storeId, name, logoPath, picturePath, type,
                                openDays, openTime, closeTime, tel, priceRate, likes, latitude,
                                longitude, description);
                        stores.add(newStore);
                        adapter.notifyDataSetChanged();
                        if (stores.size() == 0) {
                            zeroResultsLabel.setVisibility(View.VISIBLE);
                        } else zeroResultsLabel.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("json_error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("reply", "get reply error: " + error);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //back button triggered.
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
