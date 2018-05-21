package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.Adapters.ServiceCardAdapter;
import com.easypetsthailand.champ.easypets.Core.Utils;
import com.easypetsthailand.champ.easypets.Model.Groom;
import com.easypetsthailand.champ.easypets.Model.Hospital;
import com.easypetsthailand.champ.easypets.Model.Hotel;
import com.easypetsthailand.champ.easypets.Model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.Comparators.checkupComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.distanceComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.groomPriceComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.hotelPriceComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.nameComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.operationComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.popularityComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.vaccineComparator;

public class ResultActivity extends AppCompatActivity {

    private final String TAG = ResultActivity.class.getSimpleName();

    private final int REQUEST_CODE_FILTER = 159;

    private ArrayList<Service> services = new ArrayList<>();
    private ServiceCardAdapter adapter;
    private String keyword = "";
    private String sortBy = "";
    private String animals = null;

    //filters
    private boolean isOpen;

    private boolean filter(Service service) {
        return !isOpen || Utils.isOpening(service.getOpenDays(), service.getOpenTime(), service.getCloseTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILTER) {
            try {
                sortBy = data.getStringExtra("sort_by");
                isOpen = data.getBooleanExtra("is_open", isOpen);
            } catch (NullPointerException e) {
                Log.d("filterResult", "null");
            }
        }
        refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("keyword") != null) {
            keyword = getIntent().getStringExtra("keyword");
        }

        sortBy = getString(R.string.distance_untranslated);

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
                Intent intent = new Intent(ResultActivity.this, SortAndFilterActivity.class);
                intent.putExtra("type", "All");
                intent.putExtra("lastSortBy", sortBy);
                startActivityForResult(intent, REQUEST_CODE_FILTER);
            }
        });

        adapter = new ServiceCardAdapter(services, this);
        RVMenu.setLayoutManager(new LinearLayoutManager(this));
        RVMenu.setHasFixedSize(true);
        RVMenu.setAdapter(adapter);

        getBackIcon();
    }

    private void loadServices() {
        String type = "All";
        if (type.charAt(type.length() - 1) == 's')
            type = type.substring(0, type.length() - 1); //cut 's'
        String url = getString(R.string.URL) + getString(R.string.GET_SERVICE_LIST_URL, keyword, type);
        Log.d("querying service", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    Log.d(TAG + ": service response", "empty result");
                    services.clear();
                    onServiceLoaded();
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    services.clear();
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject object = array.getJSONObject(i);
                        final int serviceId = object.getInt(getString(R.string.service_id));
                        final String ownerUid = object.getString(getString(R.string.owner_uid));
                        final String name = object.getString(getString(R.string.name));
                        final String logoPath = object.getString(getString(R.string.logo_path));
                        final String picturePath = object.getString(getString(R.string.picture_path));
                        final String facebookUrl = object.getString(getString(R.string.facebook_url));
                        final String openDays = object.getString(getString(R.string.open_days));
                        String openTime = object.getString(getString(R.string.open_time));
                        if (openTime.equals("null")) openTime = null;
                        else openTime = openTime.substring(0, openTime.length() - 3);
                        String closeTime = object.getString(getString(R.string.close_time));
                        if (closeTime.equals("null")) closeTime = null;
                        else closeTime = closeTime.substring(0, closeTime.length() - 3);
                        final String tel = object.getString(getString(R.string.tel));
                        final String address = object.getString(getString(R.string.address));
                        final int likes = object.getInt(getString(R.string.likes_count));
                        final double latitude = object.getDouble(getString(R.string.latitude));
                        final double longitude = object.getDouble(getString(R.string.longitude));
                        final String description = object.getString(getString(R.string.description));

                        Service newService = new Service(serviceId, ownerUid, name
                                , logoPath, picturePath, facebookUrl, openDays
                                , openTime, closeTime, tel, address, likes
                                , latitude, longitude, description);
                        if (!filter(newService)) continue;
                        if (animals != null) {
                            int petId = -1;
                            if (animals.equalsIgnoreCase("reptiles")) petId = 1;
                            if (animals.equalsIgnoreCase("birds")) petId = 5;
                            if (animals.equalsIgnoreCase("aquatics")) petId = 6;
                            String type = "All";
                            if (type.charAt(type.length() - 1) == 's')
                                type = type.substring(0, type.length() - 1);
                            String url = getString(R.string.URL)
                                    + getString(R.string.IS_HAS_TYPE_URL, Integer.toString(serviceId), Integer.toString(petId), type);
                            Log.d("querying pets", url);
                            final Service finalNewService = newService;
                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        if (Integer.parseInt(response) == 1) {
                                            if (!services.contains(finalNewService))
                                                services.add(finalNewService);
                                            onServiceLoaded();
                                        }
                                    } catch (Exception ignored) {
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(TAG + ": Service animals", "get Service error: " + error);
                                }
                            });
                            Volley.newRequestQueue(ResultActivity.this).add(request);
                        } else services.add(newService);
                    }
                    onServiceLoaded();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("json_error", response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG + ": Service", "get Service error: " + error);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void sort() {
        if (sortBy.equalsIgnoreCase(getString(R.string.distance_untranslated))) {
            Collections.sort(services, distanceComparator);
        } else if (sortBy.equalsIgnoreCase(getString(R.string.popularity_untranslated))) {
            Collections.sort(services, popularityComparator);
        } else if (sortBy.equalsIgnoreCase(getString(R.string.name_untranslated))) {
            Collections.sort(services, nameComparator);
        } else if (sortBy.equalsIgnoreCase(getString(R.string.groom_price_untranslated))) {
            ArrayList<Groom> grooms = new ArrayList<>();
            for (Service s : services) {
                Groom g = (Groom) s;
                grooms.add(g);
            }
            Collections.sort(grooms, groomPriceComparator);
            services.clear();
            services.addAll(grooms);
        } else if (sortBy.equalsIgnoreCase(getString(R.string.hotel_price_untranslated))) {
            ArrayList<Hotel> hotels = new ArrayList<>();
            for (Service s : services) {
                Hotel h = (Hotel) s;
                hotels.add(h);
            }
            Collections.sort(hotels, hotelPriceComparator);
            services.clear();
            services.addAll(hotels);
        } else {
            ArrayList<Hospital> hospitals = new ArrayList<>();
            for (Service s : services) {
                Hospital h = (Hospital) s;
                hospitals.add(h);
            }
            if (sortBy.equalsIgnoreCase(getString(R.string.price_checkup_untranslated))) {
                Collections.sort(hospitals, checkupComparator);
            } else if (sortBy.equalsIgnoreCase(getString(R.string.price_vaccine_untranslated))) {
                Collections.sort(hospitals, vaccineComparator);
            } else if (sortBy.equalsIgnoreCase(getString(R.string.price_operation_untranslated))) {
                Collections.sort(hospitals, operationComparator);
            }
            services.clear();
            services.addAll(hospitals);
        }
    }

    private void onServiceLoaded() {
        sort();
        ArrayList<Service> t = new ArrayList<>();
        for (Service s : services) {
            boolean add = true;
            for (Service s2 : t) {
                if (s2.getServiceId() == s.getServiceId()) add = false;
            }
            if(add) t.add(s);
        }
        services.clear();
        services.addAll(t);
        adapter.notifyDataSetChanged();
        if (services.size() == 0) {
            tvZeroResults.setVisibility(View.VISIBLE);
        } else tvZeroResults.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        Log.d(TAG, "refresh");
        swipeLayout.setRefreshing(true);
        loadServices();
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    private void getBackIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

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
