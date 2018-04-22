package com.easypetsthailand.champ.easypets;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.easypetsthailand.champ.easypets.Model.Filter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.Utils.setToggleButtonBackground;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String TYPE_ALL = "all";
    private static final String TYPE_HOSPITAL = "pet hospital";
    private static final String TYPE_SERVICE = "pet service";

    @BindView(R.id.search_type)
    Spinner typeSpinner;
    @BindView(R.id.search_text)
    EditText searchFilterEditText;
    @BindView(R.id.search_filter_submit_button)
    Button searchFilterButton;
    @BindView(R.id.search_submit)
    Button searchSubmitButton;

    private String filter_type = TYPE_ALL;
    private String filter_text = "";
    private Filter filter = new Filter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.searchActivity_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navbarHeaderView = navigationView.getHeaderView(0);
        ImageView userImageView = navbarHeaderView.findViewById(R.id.navbar_imageview);
        TextView userName = navbarHeaderView.findViewById(R.id.navbar_name);
        TextView userEmail = navbarHeaderView.findViewById(R.id.navbar_email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, user + "");

        if (user != null) {
            Glide.with(this).load(user.getPhotoUrl()).fitCenter().into(userImageView);
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
        }

        searchFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog();
            }
        });
        searchSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* filter_text = searchFilterEditText.getText().toString();
                Log.d(TAG, "result is\n" +
                        "query = " + filter_text + "\n" +
                        "type = " + filter_type);
                Intent intent = new Intent(SearchActivity.this, ResultActivity_oldClass.class);
                intent.putExtra("filter_text", filter_text);
                intent.putExtra("filter_type", filter_type);
                intent.putExtra("filter", filter);
                startActivity(intent);*/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinner();
    }

    private void filterDialog() {
        final AlertDialog.Builder filterDialog = new AlertDialog.Builder(this);
        final boolean[] isFiltering = {false};
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        Log.d("load past filtering", filter.toString());
        assert inflater != null;
        View layout = inflater.inflate(R.layout.filter_layout, null);
        final Switch isOpen = layout.findViewById(R.id.filter_is_open);
        if (filter.getOpen() != null) {
            isFiltering[0] = true;
            isOpen.setChecked(filter.getOpen());
        }
        isOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setOpen(isChecked);
                isFiltering[0] = true;
            }
        });
        final ToggleButton low = layout.findViewById(R.id.lowPriceRate_toggleButton);
        if (filter.getLowPriceRateEnabled() != null) {
            isFiltering[0] = true;
            low.setChecked(filter.getLowPriceRateEnabled());
            setToggleButtonBackground(SearchActivity.this, low, filter.getLowPriceRateEnabled());
        }
        low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setLowPriceRateEnabled(isChecked);
                setToggleButtonBackground(SearchActivity.this, buttonView, isChecked);
                isFiltering[0] = true;
            }
        });
        final ToggleButton mid = layout.findViewById(R.id.midPriceRate_toggleButton);
        if (filter.getMidPriceRateEnabled() != null) {
            isFiltering[0] = true;
            mid.setChecked(filter.getMidPriceRateEnabled());
            setToggleButtonBackground(SearchActivity.this, mid, filter.getMidPriceRateEnabled());
        }
        mid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setMidPriceRateEnabled(isChecked);
                setToggleButtonBackground(SearchActivity.this, buttonView, isChecked);
                isFiltering[0] = true;
            }
        });
        final ToggleButton high = layout.findViewById(R.id.highPriceRate_toggleButton);
        if (filter.getHighPriceRateEnabled() != null) {
            isFiltering[0] = true;
            high.setChecked(filter.getHighPriceRateEnabled());
            setToggleButtonBackground(SearchActivity.this, high, filter.getHighPriceRateEnabled());
        }
        high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filter.setHighPriceRateEnabled(isChecked);
                setToggleButtonBackground(SearchActivity.this, buttonView, isChecked);
                isFiltering[0] = true;
            }
        });
        Button clear = layout.findViewById(R.id.reset_filter_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.reset();
                isFiltering[0] = false;
                low.setChecked(false);
                setToggleButtonBackground(SearchActivity.this, low, false);
                mid.setChecked(false);
                setToggleButtonBackground(SearchActivity.this, low, false);
                high.setChecked(false);
                setToggleButtonBackground(SearchActivity.this, low, false);
                isOpen.setChecked(false);
            }
        });
        filterDialog.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filter.reset();
                Log.d("FilterDialog", filter.toString());
                dialog.dismiss();
            }
        });
        filterDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isFiltering[0]) {
                    filter = new Filter();
                }
                if (!low.isChecked() && !mid.isChecked() && !high.isChecked()) {
                    filter.setLowPriceRateEnabled(null);
                    filter.setMidPriceRateEnabled(null);
                    filter.setHighPriceRateEnabled(null);
                }
                Log.d("FilterDialog", filter.toString());
                dialog.dismiss();
            }
        });
        filterDialog.setView(layout);
        filterDialog.show();
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }
        };
        adapter.addAll(getResources().getStringArray(R.array.type_list));
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    filter_type = TYPE_SERVICE;
                } else if (position == 1) {
                    filter_type = TYPE_HOSPITAL;
                }else{
                    filter_type = TYPE_ALL;
                }
                Log.d(TAG, filter_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(2);
                filter_type = TYPE_ALL;
            }
        });
        typeSpinner.setSelection(adapter.getCount());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
