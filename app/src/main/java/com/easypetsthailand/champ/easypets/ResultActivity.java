package com.easypetsthailand.champ.easypets;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.Adapters.ResultAdapter;
import com.easypetsthailand.champ.easypets.Core.GPSTracker;
import com.easypetsthailand.champ.easypets.Model.Filter;
import com.easypetsthailand.champ.easypets.Model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

import static com.easypetsthailand.champ.easypets.Core.Utils.calculateDistance;
import static com.easypetsthailand.champ.easypets.Core.Utils.isOpening;

public class ResultActivity extends AppCompatActivity {

    @BindView(R.id.result_label_zero_results)
    TextView zeroResultsLabel;
    @BindView(R.id.result_rv)
    RecyclerView resultRecyclerView;
    @BindView(R.id.fab_sort)
    FloatingTextButton fabSort;

    private ArrayList<Store> stores = new ArrayList<>();
    private ResultAdapter adapter;
    private String text;
    private String type;
    private Filter filter;
    private String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = findViewById(R.id.resultActivity_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        GPSTracker.getInstance(getApplicationContext());

        Intent i = getIntent();
        text = i.getStringExtra("filter_text");
        type = i.getStringExtra("filter_type");
        filter = (Filter) i.getSerializableExtra("filter");
        sortBy = "";

        adapter = new ResultAdapter(stores, this);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        resultRecyclerView.setHasFixedSize(true);
        resultRecyclerView.setAdapter(adapter);

        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortOptions();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        query(text, type, filter, sortBy);
        adapter.notifyDataSetChanged();
        Log.d("sort by", sortBy);
    }

    public void query(String query, String type, final Filter filter, final String sortBy) {
        if (type == null) type = "";
        if (query == null) query = "";
        String sort = sortBy;
        String filterUrl = "";
        if (filter != null && !filter.isUnset()) {
            if (filter.getLowPriceRateEnabled() == null || !filter.getLowPriceRateEnabled()) {
                //filter low price out
                filterUrl += "1";
            }
            if (filter.getMidPriceRateEnabled() == null || !filter.getMidPriceRateEnabled()) {
                //filter mid price out
                filterUrl += "2";
            }
            if (filter.getHighPriceRateEnabled() == null || !filter.getHighPriceRateEnabled()) {
                //filter high price out
                filterUrl += "3";
            }
        }
        if (sort.equals("distance") || sort.equals("popularity")) sort = "";
        String url = getString(R.string.URL) + getString(R.string.GET_STORE_URL, query, type, filterUrl, sort);
        Log.d("querying store", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
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

                        if (filter != null) {
                            if (filter.getOpen() != null && filter.getOpen() && !isOpening(openTime, closeTime)) {
                                continue;
                            }
                        }

                        Store newStore = new Store(storeId, name, logoPath, picturePath, type,
                                openDays, openTime, closeTime, tel, priceRate, likes, latitude,
                                longitude, description);
                        stores.add(newStore);
                        if(sortBy.length() == 0 || sortBy.equals("distance")) sortByDistance();
                        if (sortBy.equals("popularity")) sortByPopularity();
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

    private void sortByDistance() {
        Log.d("sorting by", "distance");
        Comparator<Store> comparator = new Comparator<Store>() {
            @Override
            public int compare(Store o1, Store o2) {
                double d1 = calculateDistance(o1.getLatitude(), o1.getLongitude());
                double d2 = calculateDistance(o2.getLatitude(), o2.getLongitude());
                return d1 < d2 ? -1
                        : d1 > d2 ? 1
                        : 0;
            }
        };
        Collections.sort(stores, comparator);
    }

    private void sortByPopularity() {
        Log.d("sorting by", "popularity");
        Comparator<Store> comparator = new Comparator<Store>() {
            @Override
            public int compare(Store o1, Store o2) {
                int l1 = o1.getLikes();
                int l2 = o2.getLikes();
                return l2 - l1;
            }
        };
        Collections.sort(stores, comparator);
    }

    private void showSortOptions() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort);
        builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        String[] choice = {getString(R.string.name_label), getString(R.string.distance)
                , getString(R.string.price), getString(R.string.popularity)};
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        sortBy = getString(R.string.name);
                        break;
                    case 1:
                        sortBy = "distance";
                        break;
                    case 2:
                        sortBy = getString(R.string.price_rate);
                        break;
                    case 3:
                        sortBy = "popularity";
                        break;
                    default:
                        sortBy = "distance";
                        break;
                }
                onResume();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //back button triggered.
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
