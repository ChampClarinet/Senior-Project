package com.easypetsthailand.champ.easypets.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easypetsthailand.champ.easypets.Adapters.ServiceCardAdapter;
import com.easypetsthailand.champ.easypets.Core.Utils;
import com.easypetsthailand.champ.easypets.Model.Service;
import com.easypetsthailand.champ.easypets.R;
import com.easypetsthailand.champ.easypets.SearchActivity;
import com.easypetsthailand.champ.easypets.SortAndFilterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.Comparators.distanceComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.nameComparator;
import static com.easypetsthailand.champ.easypets.Core.Comparators.popularityComparator;

public class SpecialPetsFragment extends Fragment {

    private static final String TAG = SpecialPetsFragment.class.getSimpleName();
    private static final String ARGS_SPECIAL_PETS = "special pets";

    private final int REQUEST_CODE_FILTER = 159;
    private final int REQUEST_CODE_SEARCH = 592;

    private int petId;
    private String keyword;
    private String sortBy;
    private ArrayList<Service> services;
    private ServiceCardAdapter adapter;

    //filters
    private boolean isOpen;

    private boolean filter(Service service) {
        return !isOpen || Utils.isOpening(service.getOpenDays(), service.getOpenTime(), service.getCloseTime());
    }

    private void loadServices() {
        /*String type = serviceType;
        if (type.charAt(type.length() - 1) == 's')
            type = type.substring(0, type.length() - 1); //cut 's'*/
        String url = getString(R.string.URL) + getString(R.string.GET_SERVICE_BY_PET_ID_URL)
                + "keyword=" + keyword + "&pet_id=" + petId;
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
                        if (filter(newService)) services.add(newService);
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
        Volley.newRequestQueue(getContext()).add(request);
    }

    @NonNull
    public static SpecialPetsFragment newInstance(String specialPets) {
        SpecialPetsFragment fragment = new SpecialPetsFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_SPECIAL_PETS, specialPets);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            String specialPets = args.getString(ARGS_SPECIAL_PETS);
            keyword = "";
            petId = -1;
            if (specialPets != null) {
                if (specialPets.equalsIgnoreCase("reptiles")) petId = 1;
                else if (specialPets.equalsIgnoreCase("birds")) petId = 5;
                else if (specialPets.equalsIgnoreCase("aquatics")) petId = 6;
            }
            services = new ArrayList<>();
        }
        sortBy = getString(R.string.distance_untranslated);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        textSearchFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = "";
                textSearchFor.setText(getString(R.string.search_for, getString(R.string.none)));
                Log.d("searchTextListener", "refreshing");
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
                Intent intent = new Intent(getContext(), SortAndFilterActivity.class);
                intent.putExtra("type", getString(R.string.title_all_uppercase));
                intent.putExtra("lastSortBy", sortBy);
                startActivityForResult(intent, REQUEST_CODE_FILTER);
            }
        });

        adapter = new ServiceCardAdapter(services, getContext());

        RVMenu.setHasFixedSize(true);
        RVMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        RVMenu.setAdapter(adapter);

        Log.d("onViewCreated", "refreshing");
        refresh();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            } catch (NullPointerException e) {
                Log.d("filterResult", "null");
            }
        }
        Log.d("AcResult", "refreshing");
        refresh();
    }

    private void sort() {
        if (sortBy == null) sortBy = getString(R.string.distance_untranslated);
        if (sortBy.equalsIgnoreCase(getString(R.string.distance_untranslated))) {
            Collections.sort(services, distanceComparator);
        } else if (sortBy.equalsIgnoreCase(getString(R.string.popularity_untranslated))) {
            Collections.sort(services, popularityComparator);
        } else if (sortBy.equalsIgnoreCase(getString(R.string.name_untranslated))) {
            Collections.sort(services, nameComparator);
        }
    }

    private void onServiceLoaded() {
        sort();
        adapter.notifyDataSetChanged();
        if (services.size() == 0) {
            tvZeroResults.setVisibility(View.VISIBLE);
        } else tvZeroResults.setVisibility(View.GONE);
    }

    private void refresh() {
        Log.d(TAG, "refresh");
        swipeLayout.setRefreshing(true);
        loadServices();
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
        if (keyword == null || keyword.length() == 0)
            textSearchFor.setText(getString(R.string.search_for, getString(R.string.none)));
        else {
            String text = getString(R.string.search_for, keyword) + " " + getString(R.string.click_for_reset);
            textSearchFor.setText(text);
        }
    }

    public SpecialPetsFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent i = new Intent(getContext(), SearchActivity.class);
            //go to search
            startActivityForResult(i, REQUEST_CODE_SEARCH);
            return true;
        }
        return false;
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
