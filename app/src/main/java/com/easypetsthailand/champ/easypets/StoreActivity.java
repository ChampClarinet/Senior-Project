package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.easypetsthailand.champ.easypets.Model.Store;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.StoreManager.checkLikeCondition;
import static com.easypetsthailand.champ.easypets.Core.Utils.dissmissLoadDialog;
import static com.easypetsthailand.champ.easypets.Core.Utils.getGoogleMapsUri;

public class StoreActivity extends AppCompatActivity {

    private final String TAG = StoreActivity.class.getSimpleName();

    @BindView(R.id.store_like_button)
    FloatingActionButton likeButton;
    @BindView(R.id.store_open_map_button)
    RelativeLayout storeOpenMapButton;
    @BindView(R.id.store_open_review_button)
    RelativeLayout storeOpenReviewButton;

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
        storeOpenMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StoreActivity.this, getString(R.string.launch_google_maps), Toast.LENGTH_SHORT).show();
                //Location location = store.getLocation();
                double latitude = 13.731595800847062;
                double longitude = 100.58154787950139;
                openGoogleMaps(latitude, longitude);
            }
        });
        storeOpenReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to review part
                Intent intent = new Intent(StoreActivity.this, ReviewActivity.class);
                intent.putExtra(getString(R.string.store_id), store_id);
                startActivity(intent);
            }
        });
    }

    private void openGoogleMaps(double latitude, double longitude) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String uri = getGoogleMapsUri(latitude, longitude);
        Log.i(TAG, uri);
        i.setData(Uri.parse(uri));
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLikeButton();
        dissmissLoadDialog();
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
