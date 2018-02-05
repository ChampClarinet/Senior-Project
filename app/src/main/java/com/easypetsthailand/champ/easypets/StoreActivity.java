package com.easypetsthailand.champ.easypets;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.StoreManager.checkLikeCondition;

public class StoreActivity extends AppCompatActivity {

    @BindView(R.id.store_like_button)
    FloatingActionButton likeButton;

    private int store_id = 1;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean isCurrentUserLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = findViewById(R.id.store_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        getBackIcon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLikeButton();
    }

    private void setLikeButton() {
        isCurrentUserLiked = checkLikeCondition(this, store_id, user.getUid());
        syncLikeCondition();
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCurrentUserLiked){
                    unlike();
                }else like();
            }
        });
    }

    private void syncLikeCondition() {
        if (isCurrentUserLiked) {
            like();
        } else unlike();
    }

    private void like() {
        Log.d("like", "like");
        isCurrentUserLiked = true;
        likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.liked)));
        likeButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
    }

    private void unlike() {
        Log.d("like", "unlike");
        isCurrentUserLiked = false;
        likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        likeButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.liked)));
    }

    private void getBackIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

}
