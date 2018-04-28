package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GPSTracker.getInstance(getApplicationContext());

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity(getString(R.string.title_hotel));
            }
        });
        groomingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity(getString(R.string.title_grooming));
            }
        });
        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity(getString(R.string.title_hospital));
            }
        });

    }

    private void openMenuActivity(String type){
        Intent i = new Intent(this, MenuActivity.class);
        i.putExtra("type", type);
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
