package com.easypetsthailand.champ.easypets;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.easypetsthailand.champ.easypets.Adapters.SpecialPetsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialPetsActivity extends AppCompatActivity {

    private static final String TAG = SpecialPetsActivity.class.getSimpleName();

    private SpecialPetsPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_pets);
        Toolbar toolbar = findViewById(R.id.special_pets_toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setTabAndPager();
        getBackIcon();
    }

    private void setTabAndPager() {
        mPagerAdapter = new SpecialPetsPagerAdapter(getSupportFragmentManager(), this);
        serviceViewPager.setAdapter(mPagerAdapter);
        serviceViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(serviceTabLayout));
        serviceTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(serviceViewPager));
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
            return false;
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

    @BindView(R.id.special_pets_pager)
    ViewPager serviceViewPager;
    @BindView(R.id.tabs)
    TabLayout serviceTabLayout;

}
