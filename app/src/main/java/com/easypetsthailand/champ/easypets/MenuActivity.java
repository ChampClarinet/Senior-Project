package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.easypetsthailand.champ.easypets.Adapters.ServiceCardAdapter;
import com.easypetsthailand.champ.easypets.Model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    private final String TAG = MenuActivity.class.getSimpleName();

    private final int REQUEST_CODE_SEARCH = 592;
    private final int REQUEST_CODE_FILTER = 159;

    private String serviceType;
    private ArrayList<Service> services = new ArrayList<>();
    private ServiceCardAdapter adapter;
    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);

        serviceType = getIntent().getStringExtra("type");
        String title = getIntent().getStringExtra("title");
        setTitle(title);

        textSearchFor.setText(getString(R.string.search_for, getString(R.string.none)));
        textSearchFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = "";
                textSearchFor.setText(getString(R.string.search_for, getString(R.string.none)));
                refresh();
            }
        });

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                refresh();
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MenuActivity.this, ServiceActivity.class), REQUEST_CODE_FILTER);
            }
        });

        adapter = new ServiceCardAdapter(services, this);
        RVMenu.setLayoutManager(new LinearLayoutManager(this));
        RVMenu.setHasFixedSize(true);
        RVMenu.setAdapter(adapter);

        getBackIcon();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH) {
            String k = "";
            try {
                k = data.getStringExtra("keyword");
                Log.d("keywordResult", k);
            } catch (NullPointerException e) {
                Log.d("keywordResult", "null");
            }
            if (k.length() > 0) {
                keyword = k;
                String text = getString(R.string.search_for, k) + " " + getString(R.string.click_for_reset);
                textSearchFor.setText(text);
                refresh();
            }
        } else if (requestCode == REQUEST_CODE_FILTER) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        Log.d(TAG, "refresh");
        swipeLayout.setRefreshing(true);
        adapter.notifyDataSetChanged();
        loadServices();
        swipeLayout.setRefreshing(false);
    }

    private void loadServices() {
        String type = serviceType;
        if (type.charAt(type.length() - 1) == 's')
            type = type.substring(0, type.length() - 1); //cut 's'
        String url = getString(R.string.URL) + getString(R.string.GET_SERVICE_LIST_URL, "", type);
        Log.d("querying service", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    Log.d("response", "empty result");
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    services.clear();
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject object = array.getJSONObject(i);
                        int serviceId = object.getInt(getString(R.string.service_id));
                        String ownerUid = object.getString(getString(R.string.owner_uid));
                        String name = object.getString(getString(R.string.name));
                        String logoPath = object.getString(getString(R.string.logo_path));
                        String picturePath = object.getString(getString(R.string.picture_path));
                        String facebookUrl = object.getString(getString(R.string.facebook_url));
                        String openDays = object.getString(getString(R.string.open_days));
                        String openTime = object.getString(getString(R.string.open_time));
                        if (openTime.equals("null")) openTime = null;
                        else openTime = openTime.substring(0, openTime.length() - 3);
                        String closeTime = object.getString(getString(R.string.close_time));
                        if (closeTime.equals("null")) closeTime = null;
                        else closeTime = closeTime.substring(0, closeTime.length() - 3);
                        String tel = object.getString(getString(R.string.tel));
                        String address = object.getString(getString(R.string.address));
                        int likes = object.getInt(getString(R.string.likes_count));
                        double latitude = object.getDouble(getString(R.string.latitude));
                        double longitude = object.getDouble(getString(R.string.longitude));
                        String description = object.getString(getString(R.string.description));

                        /*if (filter != null) {
                            if (filter.getOpen() != null && filter.getOpen() && !isOpening(openDays, openTime, closeTime)) {
                                continue;
                            }
                        }*/

                        //filter by keyword
                        if (!name.contains(keyword)) continue;

                        Service newService = new Service(serviceId, ownerUid, name, logoPath, picturePath, facebookUrl,
                                openDays, openTime, closeTime, tel, address, likes, latitude, longitude, description);
                        services.add(newService);
                        /*if(searchFor.length() == 0 || searchFor.equals("distance")) sortByDistance();
                        if (searchFor.equals("popularity")) sortByPopularity();*/
                    }
                    //services = filterAndSort(services);
                    adapter.notifyDataSetChanged();
                    if (services.size() == 0) {
                        tvZeroResults.setVisibility(View.VISIBLE);
                    } else tvZeroResults.setVisibility(View.GONE);
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

    //define sorting options dialog
    private void showSortOptions() {
        /*
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort);
        builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        String[] choice = {getString(R.string.sort_name), getString(R.string.sort_distance)
                , getString(R.string.sort_price), getString(R.string.sort_popularity)};
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        searchFor = getString(R.string.sort_name);
                        break;
                    case 1:
                        searchFor = getString(R.string.sort_distance);
                        break;
                    case 2:
                        searchFor = getString(R.string.sort_price);
                        break;
                    case 3:
                        searchFor = getString(R.string.sort_popularity);
                        break;
                    default:
                        searchFor = getString(R.string.sort_distance);
                        break;
                }
                textSearchFor.setText(getString(R.string.search_for, searchFor));
                onResume();
                dialog.dismiss();
            }
        });
        builder.show();
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.text), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent i = new Intent(this, SearchActivity.class);
            //go to search
            startActivityForResult(i, REQUEST_CODE_SEARCH);
        } else if (id == android.R.id.home) {
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

    /*@BindView(R.id.fab_sort_by)
    FloatingTextButton textSearchFor;*/
    @BindView(R.id.search_for)
    TextView textSearchFor;
    @BindView(R.id.filter_button)
    TextView filterButton;
    @BindView(R.id.menu_activity_rv)
    RecyclerView RVMenu;
    @BindView(R.id.menu_activity_label_zero_results)
    TextView tvZeroResults;
    @BindView(R.id.menu_swipe_layout)
    SwipeRefreshLayout swipeLayout;

}
