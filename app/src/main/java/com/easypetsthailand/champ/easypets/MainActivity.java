package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.easypetsthailand.champ.easypets.Core.GPSTracker;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.title_deposit)
    ConstraintLayout depositButton;
    @BindView(R.id.title_grooming)
    ConstraintLayout groomingButton;
    @BindView(R.id.title_hospital)
    ConstraintLayout hospitalButton;
    @BindView(R.id.title_all)
    ConstraintLayout searchAll;
    @BindView(R.id.title_other)
    ConstraintLayout searchOther;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GPSTracker.getInstance(getApplicationContext());

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity(getString(R.string.title_hotel), getString(R.string.title_hotel_uppercase));
            }
        });
        groomingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity(getString(R.string.title_grooming), getString(R.string.title_grooming_uppercase));
            }
        });
        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity(getString(R.string.title_hospital), getString(R.string.title_hospital_uppercase));
            }
        });
        searchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity(getString(R.string.title_all), getString(R.string.title_all_uppercase));
            }
        });
        searchOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchSpecialActivity.class));
            }
        });

    }

    private void openMenuActivity(String type, String title){
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("type", type);
        i.putExtra("title", title);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
