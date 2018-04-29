package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.Adapters.ServiceCardAdapter;
import com.easypetsthailand.champ.easypets.Model.Groom;
import com.easypetsthailand.champ.easypets.Model.Hospital;
import com.easypetsthailand.champ.easypets.Model.Hotel;
import com.easypetsthailand.champ.easypets.Model.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.Utils.calculateDistance;

public class MenuActivity extends AppCompatActivity {

    private final String TAG = MenuActivity.class.getSimpleName();

    private final int REQUEST_CODE_SEARCH = 592;
    private final int REQUEST_CODE_FILTER = 159;

    private String serviceType;
    private ArrayList<Service> services = new ArrayList<>();
    private ServiceCardAdapter adapter;
    private String keyword = "";
    private String sortBy = "";

    //filters
    private boolean isOpen;
    private boolean isAcceptOvernight;
    private boolean isAcceptOperation;
    private int min;
    private int max;
    private int checkUpMin;
    private int checkUpMax;
    private int vaccineMin;
    private int vaccineMax;
    private int operationMin;
    private int operationMax;

    private boolean filter(Groom groom) {
        return true;
    }

    private boolean filter(Hospital hospital) {
        return true;
    }

    private boolean filter(Hotel hotel) {
        return true;
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
            }
        } else if (requestCode == REQUEST_CODE_FILTER) {
            try {
                sortBy = data.getStringExtra("sort_by");
                isOpen = data.getBooleanExtra("is_open", isOpen);
                if (serviceType.equalsIgnoreCase(getString(R.string.title_grooming))) {
                    min = data.getIntExtra("groom_min_price", min);
                    max = data.getIntExtra("groom_max_price", max);
                } else if (serviceType.equalsIgnoreCase(getString(R.string.title_hotel))) {
                    isAcceptOvernight = data.getBooleanExtra("is_accept_overnight", isAcceptOvernight);
                    min = data.getIntExtra("hotel_min_price", min);
                    max = data.getIntExtra("hotel_max_price", max);
                } else if (serviceType.equalsIgnoreCase(getString(R.string.title_hospital))) {
                    isAcceptOperation = data.getBooleanExtra("is_accept_operation", isAcceptOperation);
                    checkUpMin = data.getIntExtra("checkup_min_price", checkUpMin);
                    checkUpMax = data.getIntExtra("checkup_max_price", checkUpMax);
                    vaccineMin = data.getIntExtra("vaccine_min_price", vaccineMin);
                    vaccineMax = data.getIntExtra("vaccine_max_price", vaccineMax);
                    operationMin = data.getIntExtra("operation_min_price", operationMin);
                    operationMax = data.getIntExtra("operation_max_price", operationMax);
                }
                /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Filters");
                String message = "isOpen = " + isOpen + "\n" +
                        "isAcceptOvernight = " + isAcceptOvernight + "\n" +
                        "isAcceptOperation = " + isAcceptOperation + "\n" +
                        "min = " + min + "\n" +
                        "max = " + max + "\n" +
                        "checkUpMin = " + checkUpMin + "\n" +
                        "checkUpMax = " + checkUpMax + "\n" +
                        "vaccineMin = " + vaccineMin + "\n" +
                        "vaccineMax = " + vaccineMax + "\n" +
                        "operationMin = " + operationMin + "\n" +
                        "operationMax = " + operationMax;
                builder.setMessage(message);
                builder.show();*/
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

        serviceType = getIntent().getStringExtra("type");
        String title = getIntent().getStringExtra("title");
        setTitle(title);

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
                Intent intent = new Intent(MenuActivity.this, SortAndFilterActivity.class);
                intent.putExtra("type", serviceType);
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

    private void loadServices() {
        String type = serviceType;
        if (type.charAt(type.length() - 1) == 's')
            type = type.substring(0, type.length() - 1); //cut 's'
        String url = getString(R.string.URL) + getString(R.string.GET_SERVICE_LIST_URL, keyword, type);
        Log.d("querying service", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    Log.d(TAG + ": service response", "empty result");
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

                        if (serviceType.equalsIgnoreCase(getString(R.string.title_grooming))) {
                            int groomPrice = object.getInt("grooming_price_rate");
                            Groom newService = new Groom(serviceId, ownerUid, name
                                    , logoPath, picturePath, facebookUrl, openDays
                                    , openTime, closeTime, tel, address, likes
                                    , latitude, longitude, description, groomPrice);
                            if(filter(newService)) services.add(newService);
                        } else if (serviceType.equalsIgnoreCase(getString(R.string.title_hospital))) {
                            int isAcceptBigOperation = object.getInt("is_accept_big_operation");
                            int checkupPriceRate = object.getInt("checkup_price_rate");
                            int vaccinePriceRate = object.getInt("vaccine_price_rate");
                            int operationPriceRate = object.getInt("operation_price_rate");
                            Hospital newService = new Hospital(serviceId, ownerUid, name
                                    , logoPath, picturePath, facebookUrl, openDays
                                    , openTime, closeTime, tel, address, likes
                                    , latitude, longitude, description
                                    , isAcceptBigOperation, checkupPriceRate
                                    , vaccinePriceRate, operationPriceRate);
                            if(filter(newService)) services.add(newService);
                        } else if (serviceType.equalsIgnoreCase(getString(R.string.title_hotel))) {
                            int isAcceptOvernight = object.getInt("is_accept_overnight");
                            int hotelPrice = object.getInt("hotel_price");
                            Hotel newService = new Hotel(serviceId, ownerUid, name
                                    , logoPath, picturePath, facebookUrl, openDays
                                    , openTime, closeTime, tel, address, likes
                                    , latitude, longitude, description
                                    , isAcceptOvernight, hotelPrice);
                            if(filter(newService)) services.add(newService);
                        }
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

    private void onServiceLoaded() {
        sort();
        adapter.notifyDataSetChanged();
        if (services.size() == 0) {
            tvZeroResults.setVisibility(View.VISIBLE);
        } else tvZeroResults.setVisibility(View.GONE);
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

    private final Comparator<Service> distanceComparator = new Comparator<Service>() {
        @Override
        public int compare(Service o1, Service o2) {
            Double d1 = calculateDistance(o1.getLatitude(), o1.getLongitude());
            Double d2 = calculateDistance(o2.getLatitude(), o2.getLongitude());
            return d1.compareTo(d2);
        }
    };

    private final Comparator<Service> popularityComparator = new Comparator<Service>() {
        @Override
        public int compare(Service o1, Service o2) {
            return o2.getLikes() - o1.getLikes();
        }
    };

    private final Comparator<Service> nameComparator = new Comparator<Service>() {
        @Override
        public int compare(Service o1, Service o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    private final Comparator<Groom> groomPriceComparator = new Comparator<Groom>() {
        @Override
        public int compare(Groom o1, Groom o2) {
            return o1.getPriceRate() - o2.getPriceRate();
        }
    };

    private final Comparator<Hotel> hotelPriceComparator = new Comparator<Hotel>() {
        @Override
        public int compare(Hotel o1, Hotel o2) {
            return o1.getHotelPrice() - o2.getHotelPrice();
        }
    };

    private final Comparator<Hospital> checkupComparator = new Comparator<Hospital>() {
        @Override
        public int compare(Hospital o1, Hospital o2) {
            return o1.getCheckupPriceRate() - o2.getCheckupPriceRate();
        }
    };

    private final Comparator<Hospital> vaccineComparator = new Comparator<Hospital>() {
        @Override
        public int compare(Hospital o1, Hospital o2) {
            return o1.getVaccinePriceRate() - o2.getVaccinePriceRate();
        }
    };

    private final Comparator<Hospital> operationComparator = new Comparator<Hospital>() {
        @Override
        public int compare(Hospital o1, Hospital o2) {
            return o1.getOperationPriceRate() - o2.getOperationPriceRate();
        }
    };

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
