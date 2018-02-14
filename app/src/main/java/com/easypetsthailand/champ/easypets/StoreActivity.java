package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.easypetsthailand.champ.easypets.Core.StoreManager;
import com.easypetsthailand.champ.easypets.Model.Store;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.StoreManager.bindReviewsCountToTextView;
import static com.easypetsthailand.champ.easypets.Core.Utils.dissmissLoadDialog;
import static com.easypetsthailand.champ.easypets.Core.Utils.getGoogleMapsUri;
import static com.easypetsthailand.champ.easypets.Core.Utils.isOpening;

public class StoreActivity extends AppCompatActivity {

    private final String TAG = StoreActivity.class.getSimpleName();

    @BindView(R.id.store_like_button)
    FloatingActionButton likeButton;
    @BindView(R.id.store_open_map_button)
    RelativeLayout storeOpenMapButton;
    @BindView(R.id.store_open_review_button)
    RelativeLayout storeOpenReviewButton;
    @BindView(R.id.imgView_store)
    ImageView imageViewStorePicture;
    @BindView(R.id.store_time_open)
    TextView textViewTimeOpen;
    @BindView(R.id.store_is_open)
    TextView textViewIsOpen;
    @BindView(R.id.store_tel)
    TextView textViewTel;
    @BindView(R.id.store_tv_review_count)
    TextView textViewReviewCount;
    @BindView(R.id.store_desc)
    TextView textViewStoreDesc;

    private Store store;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean isCurrentUserLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = findViewById(R.id.store_toolbar);
        setSupportActionBar(toolbar);

        store = (Store) getIntent().getSerializableExtra(getString(R.string.model_name_store));
        Log.d("store1", String.valueOf(store == null));

        ButterKnife.bind(this);
        getBackIcon();
        storeOpenMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StoreActivity.this, getString(R.string.launch_google_maps), Toast.LENGTH_SHORT).show();
                openGoogleMaps(store.getLatitude(), store.getLongitude());
            }
        });
        storeOpenReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to review part
                Intent intent = new Intent(StoreActivity.this, ReviewActivity.class);
                intent.putExtra(getString(R.string.model_name_store), store);
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
        setData();
    }

    private void setData() {
        //name
        setTitle(store.getName());
        //image
        String picturePath = getString(R.string.pictures_storage_ref) + store.getPicturePath();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(picturePath);
        Glide.with(this).using(new FirebaseImageLoader()).load(reference).centerCrop()
                .placeholder(android.R.drawable.ic_menu_report_image).into(imageViewStorePicture);
        //time
        String openTime = store.getOpenTime();
        String closeTime = store.getCloseTime();
        boolean isOpening = isOpening(openTime, closeTime);
        if (isOpening) {
            textViewIsOpen.setText(getString(R.string.open));
            textViewIsOpen.setTextColor(Color.GREEN);
        } else {
            textViewIsOpen.setText(getString(R.string.close));
            textViewIsOpen.setTextColor(Color.RED);
        }
        if (openTime != null && closeTime != null)
            textViewTimeOpen.setText(getString(R.string.time_open_label, openTime, closeTime));
        else textViewTimeOpen.setText(getString(R.string.all_day_open));
        //tel
        textViewTel.setText(store.getTel());
        //reviews
        bindReviewsCountToTextView(this, store.getStoreId(), textViewReviewCount);
        //desc
        textViewStoreDesc.setText(store.getDescription());
    }

    private void setLikeButton() {
        final String url = getString(R.string.URL) + getString(R.string.LIKE_CONDITION_URL, store.getStoreId(), user.getUid());
        Log.d("check like", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("true")) {
                    isCurrentUserLiked = true;
                } else if (response.equals("false")) {
                    isCurrentUserLiked = false;
                }
                Log.d(TAG, response);
                syncLikeCondition();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error\n"+url);
            }
        });
        Volley.newRequestQueue(this).add(request);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCurrentUserLiked){
                    unlike();
                }else like();
                StoreManager.like(StoreActivity.this, store.getStoreId(), user.getUid());
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
