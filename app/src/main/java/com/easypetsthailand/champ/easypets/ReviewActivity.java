package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.Adapters.ReviewAdapter;
import com.easypetsthailand.champ.easypets.Model.Review;
import com.easypetsthailand.champ.easypets.Model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity {

    private final String TAG = ReviewActivity.class.getSimpleName();
    private ArrayList<Review> reviews = new ArrayList<>();
    private ReviewAdapter adapter;
    private Store store;
    private RequestQueue requestQueue;

    @BindView(R.id.rv_review)
    RecyclerView rvReview;
    @BindView(R.id.tv_no_review)
    TextView tvNoReview;
    @BindView(R.id.fab_review)
    FloatingActionButton reviewFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = findViewById(R.id.reviewActivity_toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getBackIcon();

        reviewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, WriteReviewActivity.class);
                intent.putExtra("store_id", store.getStoreId());
                intent.putExtra("review_count", reviews.size());
                startActivity(intent);
            }
        });

        store = (Store) getIntent().getSerializableExtra(getString(R.string.model_name_store));
        setTitle(getString(R.string.reviews, ""));

        requestQueue = Volley.newRequestQueue(this);

        adapter = new ReviewAdapter(reviews, store, requestQueue, this);
        rvReview.setAdapter(adapter);
        rvReview.setHasFixedSize(true);
        rvReview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (store != null) bindData();
    }

    private void bindData() {
        String url = getString(R.string.URL) + getString(R.string.GET_REVIEW_BY_STORE_ID_URL, store.getStoreId());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    reviews.clear();
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject o = array.getJSONObject(i);
                        Review r = new Review(
                                o.getInt("review_id"),
                                o.getString("reviewer_uid"),
                                o.getString("review_text"),
                                o.getString("review_picture_path"),
                                o.getString("time_reviewed")
                        );
                        Log.d(TAG, "importing" + r.getReviewId() + " : " + o.getString("time_reviewed"));
                        reviews.add(r);
                        adapter.notifyDataSetChanged();
                        if (reviews.size() > 0) {
                            rvReview.setVisibility(View.VISIBLE);
                            tvNoReview.setVisibility(View.GONE);
                        } else {
                            rvReview.setVisibility(View.GONE);
                            tvNoReview.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "error " + response);
                    Log.d("jsonException", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        request.setTag("review");
        requestQueue.add(request);
    }

    private void getBackIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        requestQueue.cancelAll("review");
    }
}
