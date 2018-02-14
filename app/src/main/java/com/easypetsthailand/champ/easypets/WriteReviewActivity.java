package com.easypetsthailand.champ.easypets;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myhexaville.smartimagepicker.ImagePicker;
import com.myhexaville.smartimagepicker.OnImagePickedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WriteReviewActivity extends AppCompatActivity implements OnImagePickedListener /*implements ImageChooserListener */ {

    private final String TAG = WriteReviewActivity.class.getSimpleName();

    @BindView(R.id.new_review_text)
    EditText newReviewEditText;
    @BindView(R.id.add_photo_button)
    LinearLayout addPhotoButton;
    @BindView(R.id.new_review_image)
    ImageView newReviewImageView;
    @BindView(R.id.delete_review_image)
    ImageView deleteImage;
    @BindView(R.id.chosen_image_frame)
    FrameLayout imageFrame;

    private ImagePicker imagePicker;
    private String picturePath = null;
    private int new_review_id;
    private int store_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        Toolbar toolbar = findViewById(R.id.replyActivity_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        new_review_id = getIntent().getIntExtra("reviews_count", -1) + 1;
        store_id = getIntent().getIntExtra("store_id", -1);

        imagePicker = new ImagePicker(this,
                null, this);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(WriteReviewActivity.this).setItems(R.array.image_chooser_options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                chooseImage();
                                break;
                            case 1:
                                takePhoto();
                                break;
                        }
                    }
                })
                        .show();
            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFrame.setVisibility(View.GONE);
                addPhotoButton.setVisibility(View.VISIBLE);
            }
        });

    }

    private void post() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String text = newReviewEditText.getText().toString();
        if(isReviewEditTextEmpty()){
            Toast.makeText(this, getString(R.string.message_fill_review), Toast.LENGTH_SHORT).show();
            return;
        }
        if(isPicturePathEmpty()){
            picturePath = "null";
        }
        String url = getString(R.string.URL) + getString(R.string.POST_REVIEW_URL, store_id, uid, text, picturePath);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("post response", response);
                if(response.equals("200")){
                    Toast.makeText(WriteReviewActivity.this, getString(R.string.post_success), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(WriteReviewActivity.this, getString(R.string.post_failed), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("post review error", error.toString());
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private boolean isPicturePathEmpty() {
        return picturePath == null;
    }

    private boolean isReviewEditTextEmpty() {
        return newReviewEditText.getText().toString().length() == 0;
    }

    @Override
    public void onImagePicked(Uri imageUri) {
        Glide.with(this).load(imageUri).into(newReviewImageView);
        imageFrame.setVisibility(View.VISIBLE);
        addPhotoButton.setVisibility(View.GONE);

        picturePath = "review_" + new_review_id + "_" + imageUri.getPath().substring(imageUri.getPath().lastIndexOf("/") + 1);

        String reviewImageRef = getString(R.string.review_images_storage_ref) + picturePath;

        Log.d("path", reviewImageRef);
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(reviewImageRef);
        UploadTask uploadTask = reference.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "uploaded");
            }
        });
    }

    private void takePhoto() {
        imagePicker.openCamera();
    }

    private void chooseImage() {
        imagePicker.choosePicture(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        imagePicker.handleActivityResult(resultCode, requestCode, returnedIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_write_review, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_post) {
            post();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
