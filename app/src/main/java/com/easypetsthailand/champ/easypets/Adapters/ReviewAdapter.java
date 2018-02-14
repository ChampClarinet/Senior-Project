package com.easypetsthailand.champ.easypets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.easypetsthailand.champ.easypets.Model.Review;
import com.easypetsthailand.champ.easypets.Model.Store;
import com.easypetsthailand.champ.easypets.R;
import com.easypetsthailand.champ.easypets.ReplyActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.GenericHolder> {

    private ArrayList<Review> dataSet;
    private Store store;
    private Context context;
    private RequestQueue requestQueue;

    public ReviewAdapter(ArrayList<Review> dataSet, Store store, RequestQueue requestQueue, Context context) {
        this.dataSet = dataSet;
        this.store = store;
        this.requestQueue = requestQueue;
        this.context = context;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(v, store, requestQueue, context);
    }

    @Override
    public void onBindViewHolder(GenericHolder holder, int position) {
        Review review = dataSet.get(position);
        holder.setViewData(review);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public ArrayList getData() {
        return dataSet;
    }

    public abstract static class GenericHolder extends RecyclerView.ViewHolder {

        public GenericHolder(View itemView) {
            super(itemView);
        }

        abstract public void setViewData(Review review);

    }

    public static class ViewHolder extends GenericHolder {

        //bind cardview's widgets
        @BindView(R.id.cv_post_card)
        CardView cardView;
        @BindView(R.id.tv_poster_name)
        TextView cv_name;
        @BindView(R.id.imgview_poster_pic)
        ImageView cv_reviewer_pic;
        @BindView(R.id.tv_post_time)
        TextView cv_reviewTime;
        @BindView(R.id.tv_post_text)
        TextView cv_reviewText;
        @BindView(R.id.img_post_image)
        ImageView img_review_image;
        private Context context;
        private Store store;
        private RequestQueue requestQueue;

        public ViewHolder(View itemView, Store store, RequestQueue requestQueue, Context context) {
            super(itemView);
            this.context = context;
            this.store = store;
            this.requestQueue = requestQueue;
            ButterKnife.bind(this, itemView);
        }

        private void openStoreActivity(Review review, String reviewerName, String reviewerPicturePath) throws Exception {
            Intent i = new Intent(context, ReplyActivity.class);
            i.putExtra(context.getString(R.string.model_name_store), store);
            i.putExtra("review", review);
            i.putExtra("reviewerName", reviewerName);
            i.putExtra("reviewerPicturePath", reviewerPicturePath);
            context.startActivity(i);
        }

        @Override
        public void setViewData(final Review review) {
            final String[] user = {null, null};
            String url = context.getString(R.string.URL) + context.getString(R.string.GET_USER_BY_UID_URL, review.getReviewerUid());
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject o = new JSONObject(response);
                        String reviewerName = o.getString("name");
                        String reviewerPicturePath = o.getString("picture_path");
                        user[0] = reviewerName;
                        user[1] = reviewerPicturePath;

                        //cv_name
                        cv_name.setText(reviewerName);

                        //user pic
                        Glide.with(context).load(reviewerPicturePath).centerCrop().into(cv_reviewer_pic);
                    } catch (JSONException e) {
                        Log.d("user load error", response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("loading user", error.toString());
                }
            });
            request.setTag("review");
            requestQueue.add(request);

            //time
            cv_reviewTime.setText(review.getTimeReviewed());

            //review text
            cv_reviewText.setText(review.getReviewText());

            //review image
            Log.d("reviewPic", review.getReviewPicturePath());
            if(!review.getReviewPicturePath().equalsIgnoreCase("null")){
                String imagePath = context.getString(R.string.review_images_storage_ref) + review.getReviewPicturePath();
                StorageReference reference = FirebaseStorage.getInstance().getReference().child(imagePath);
                Glide.with(context).using(new FirebaseImageLoader()).load(reference).centerCrop()
                        .placeholder(android.R.drawable.ic_menu_report_image).into(img_review_image);
                img_review_image.setVisibility(View.VISIBLE);
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        openStoreActivity(review, user[0], user[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String printList() {
        String out = "";
        for (Review r : dataSet) out += r.getReviewText() + ", ";
        return out;
    }

}
