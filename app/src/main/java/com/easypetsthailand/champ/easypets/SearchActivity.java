package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.Utils.createLoadDialog;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private static final String TYPE_ALL = "all";
    private static final String TYPE_HOSPITAL = "hospital";
    private static final String TYPE_SERVICE = "service";

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

    private AlertDialog dialog;

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

        setSpinner();
        searchFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SearchActivity.this, FilterActivity.class), 1234);
            }
        });
        searchSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_text = searchFilterEditText.getText().toString();
                Log.d(TAG, "result is\n" +
                        "query = " + filter_text + "\n" +
                        "type = " + filter_type);
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                intent.putExtra("filter_text", filter_text);
                intent.putExtra("filter_type", filter_type);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null) dialog.dismiss();
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.type_list, android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    filter_type = TYPE_ALL;
                } else if (position == 1) {
                    filter_type = TYPE_SERVICE;
                } else if (position == 2) {
                    filter_type = TYPE_HOSPITAL;
                }
                Log.d(TAG, filter_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            //set filter
        }
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
