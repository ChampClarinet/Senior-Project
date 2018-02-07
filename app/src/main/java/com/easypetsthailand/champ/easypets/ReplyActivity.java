package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.easypetsthailand.champ.easypets.Adapters.ReplyAdapter;
import com.easypetsthailand.champ.easypets.Model.Reply;
import com.easypetsthailand.champ.easypets.Model.Review;
import com.easypetsthailand.champ.easypets.Model.Store;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReplyActivity extends AppCompatActivity {

    private final String TAG = ReplyActivity.class.getSimpleName();

    private ReplyAdapter adapter;
    private ArrayList<Reply> replies;

    @BindView(R.id.reply_imgview_user_pic)
    ImageView reviewerImageView;
    @BindView(R.id.reply_tv_reviewer_name)
    TextView reviewerName;
    @BindView(R.id.reply_tv_review_time)
    TextView reviewTime;
    @BindView(R.id.reply_tv_review_text)
    TextView reviewText;
    @BindView(R.id.reply_tv_reply_label)
    TextView replyLabel;
    @BindView(R.id.rv_replies)
    RecyclerView rvReplies;

    private Review review;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Toolbar toolbar = findViewById(R.id.replyActivity_toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        review = (Review) i.getSerializableExtra("review");
        store = (Store) i.getSerializableExtra(getString(R.string.model_name_store));

        ButterKnife.bind(this);
        bindReviews();
        bindReplies();
    }

    private void bindReplies() {
        replies = new ArrayList<>();
        adapter = new ReplyAdapter(replies, store, this);
        rvReplies.setHasFixedSize(true);
        rvReplies.setLayoutManager(new LinearLayoutManager(this));
        rvReplies.setAdapter(adapter);

        String url = getString(R.string.URL) + getString(R.string.GET_REPLIES_BY_REVIEW_ID_URL, review.getReviewId());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    replies.clear();
                    for(int i=0;i<array.length();++i){
                        JSONObject o = array.getJSONObject(i);
                        Reply r = new Reply(
                                o.getInt("reply_id"),
                                o.getString("reply_text"),
                                o.getString("reply_picture_path"),
                                o.getString("time_replied")
                        );
                        Log.d(TAG, "importing"+r.getReplyId()+" : "+o.getString("time_replied"));
                        replies.add(r);
                        adapter.notifyDataSetChanged();
                        if(replies.size() > 0){
                            replyLabel.setText(R.string.replies);
                        }else{
                            replyLabel.setText(R.string.no_replies);
                        }
                    }
                }catch (JSONException e){
                    Log.d(TAG, "error "+response);
                    Log.d("jsonException", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        Volley.newRequestQueue(this).add(request);

    }

    private void bindReviews() {
        String reviewerName = getIntent().getStringExtra("reviewerName");
        String reviewerPicturePath = getIntent().getStringExtra("reviewerPicturePath");
        this.reviewerName.setText(reviewerName);
        Glide.with(this).load(reviewerPicturePath).centerCrop().into(this.reviewerImageView);
        reviewTime.setText(review.getTimeReviewed());
        reviewText.setText(review.getReviewText());
    }
}
