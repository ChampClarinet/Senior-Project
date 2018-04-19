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
import com.easypetsthailand.champ.easypets.Core.Utils;
import com.easypetsthailand.champ.easypets.Model.Service.Service;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.StoreManager.bindReviewsCountToTextView;
import static com.easypetsthailand.champ.easypets.Core.Utils.dissmissLoadDialog;
import static com.easypetsthailand.champ.easypets.Core.Utils.getGoogleMapsUri;
import static com.easypetsthailand.champ.easypets.Core.Utils.isOpening;

public class ServiceActivity extends AppCompatActivity {

    private final String TAG = ServiceActivity.class.getSimpleName();

    @BindView(R.id.service_like_button)
    FloatingActionButton likeButton;
    @BindView(R.id.service_open_map_button)
    RelativeLayout serviceOpenMapButton;
    @BindView(R.id.service_open_review_button)
    RelativeLayout serviceOpenReviewButton;
    @BindView(R.id.service_acceptance)
    TextView textViewPetAcceptance;
    @BindView(R.id.imgView_service)
    ImageView imageViewStorePicture;
    @BindView(R.id.service_time_open)
    TextView textViewTimeOpen;
    @BindView(R.id.service_is_open)
    TextView textViewIsOpen;
    @BindView(R.id.service_tel)
    TextView textViewTel;
    @BindView(R.id.service_address)
    TextView textViewAddress;
    @BindView(R.id.service_tv_review_count)
    TextView textViewReviewCount;
    @BindView(R.id.service_desc)
    TextView textViewStoreDesc;

    private Service service;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean isCurrentUserLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = findViewById(R.id.store_toolbar);
        setSupportActionBar(toolbar);

        service = (Service) getIntent().getSerializableExtra(getString(R.string.model_name_service));
        Log.d(TAG + ": is service null", String.valueOf(service == null));

        getIntent().putExtra("lizard", getString(R.string.lizard));
        getIntent().putExtra("pig", getString(R.string.pig));
        getIntent().putExtra("snake", getString(R.string.snake));
        getIntent().putExtra("fennec fox", getString(R.string.fennec_fox));

        ButterKnife.bind(this);
        getBackIcon();
        serviceOpenMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceActivity.this, getString(R.string.launch_google_maps), Toast.LENGTH_SHORT).show();
                openGoogleMaps(service.getLatitude(), service.getLongitude());
            }
        });
        serviceOpenReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to review part
                Intent intent = new Intent(ServiceActivity.this, ReviewActivity.class);
                intent.putExtra(getString(R.string.model_name_service), service);
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
        setTitle(service.getName());
        //image
        String picturePath = getString(R.string.pictures_storage_ref) + service.getPicturePath();
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(picturePath);
        Glide.with(this).using(new FirebaseImageLoader()).load(reference).centerCrop()
                .placeholder(android.R.drawable.ic_menu_report_image).into(imageViewStorePicture);
        Log.d("picture location", reference.toString());

        setSpecialPets();

        //time
        boolean[] openDays = service.getOpenDays();
        String openTime = service.getOpenTime();
        String closeTime = service.getCloseTime();
        boolean isOpening = isOpening(openDays, openTime, closeTime);
        String timeLabel = "";
        if (isOpening) {
            textViewIsOpen.setText(getString(R.string.open));
            textViewIsOpen.setTextColor(Color.GREEN);
        } else {
            textViewIsOpen.setText(getString(R.string.close));
            textViewIsOpen.setTextColor(Color.RED);
            timeLabel += Utils.getNextOpens(service, this);
        }
        if (openTime != null && closeTime != null)
            timeLabel += getString(R.string.time_open_label, openTime, closeTime);
        else timeLabel += getString(R.string.all_day_open);
        textViewTimeOpen.setText(timeLabel);
        /*
        boolean[] openDays = service.getOpenDays();
        String openTime = service.getOpenTime();
        String closeTime = service.getCloseTime();
        boolean isOpening = isOpening(openDays, openTime, closeTime);
        if (isOpening) {
            textViewIsOpen.setText(getString(R.string.open));
            textViewIsOpen.setTextColor(Color.GREEN);
        } else {
            textViewIsOpen.setText(getString(R.string.close));
            textViewIsOpen.setTextColor(Color.RED);
        }
        if (openTime != null && closeTime != null)
            textViewTimeOpen.setText(getString(R.string.time_open_label, openTime, closeTime));
        else textViewTimeOpen.setText(getString(R.string.all_day_open));*/
        //tel
        textViewTel.setText(service.getTel());
        //address
        textViewAddress.setText(service.getAddress());
        //reviews
        bindReviewsCountToTextView(this, service.getServiceId(), textViewReviewCount);
        //desc
        textViewStoreDesc.setText(service.getDescription());
    }

    private void setSpecialPets() {
        final String url = getString(R.string.URL) + getString(R.string.GET_AVAILABLE_PETS_FOR_SERVICE_URL, Integer.toString(service.getServiceId()));
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("404")) {
                    textViewPetAcceptance.setText(getString(R.string.none));
                    return;
                }
                try {
                    JSONArray array = new JSONArray(response);
                    String s = "";
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject object = array.getJSONObject(i);
                        String pet = object.getString(getString(R.string.pet_name));
                        if (pet != null && pet.length() > 0) {
                            if (s.length() != 0) s += ", ";
                            s += getIntent().getStringExtra(pet);
                        }
                    }
                    textViewPetAcceptance.setText(s);
                } catch (JSONException e) {
                    Log.d(TAG, "special pet json error");
                    textViewPetAcceptance.setText(getString(R.string.none));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error on " + url);
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void setLikeButton() {
        final String url = getString(R.string.URL) + getString(R.string.LIKE_CONDITION_URL, service.getServiceId(), user.getUid());
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
                Log.d(TAG, "error\n" + url);
            }
        });
        Volley.newRequestQueue(this).add(request);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCurrentUserLiked) {
                    unlike();
                } else like();
                StoreManager.like(ServiceActivity.this, service.getServiceId(), user.getUid());
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
        likeButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.unliked)));
    }

    private void unlike() {
        Log.d("like", "unlike");
        isCurrentUserLiked = false;
        likeButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.unliked)));
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
