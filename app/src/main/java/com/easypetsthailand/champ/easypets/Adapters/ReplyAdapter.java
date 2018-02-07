package com.easypetsthailand.champ.easypets.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.easypetsthailand.champ.easypets.Model.Reply;
import com.easypetsthailand.champ.easypets.Model.Review;
import com.easypetsthailand.champ.easypets.Model.Store;
import com.easypetsthailand.champ.easypets.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.GenericHolder> {

    private Store store;
    private ArrayList<Reply> dataSet;
    private Context context;

    public ReplyAdapter(ArrayList<Reply> dataSet, Store store, Context context) {
        this.dataSet = dataSet;
        this.store = store;
        this.context = context;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_review, parent, false);
        return new ViewHolder(v, store, context);
    }

    @Override
    public void onBindViewHolder(GenericHolder holder, int position) {
        Reply reply = dataSet.get(position);
        holder.setViewData(reply);
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

        abstract public void setViewData(Reply reply);

    }

    public static class ViewHolder extends GenericHolder {

        //bind cardview's widgets
        @BindView(R.id.cv_review_card)
        CardView cardView;
        @BindView(R.id.tv_reviewer_name)
        TextView cv_name;
        @BindView(R.id.imgview_reviewer_pic)
        ImageView cv_reviewer_pic;
        @BindView(R.id.tv_review_time)
        TextView cv_reviewTime;
        @BindView(R.id.tv_review_text)
        TextView cv_reviewText;
        private Context context;
        private Store store;

        public ViewHolder(View itemView, Store store, Context context) {
            super(itemView);
            this.context = context;
            this.store = store;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setViewData(final Reply reply) {
            String replierName = store.getName();
            String replierPicturePath = context.getString(R.string.icon_storage_ref) + store.getLogoPath();
            Log.d("path", replierPicturePath);

            //cv_name
            cv_name.setText(replierName);

            //user pic
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(replierPicturePath);
            Glide.with(context).using(new FirebaseImageLoader()).load(reference).into(cv_reviewer_pic);

            //time
            cv_reviewTime.setText(reply.getTimeReplied());

            //review text
            cv_reviewText.setText(reply.getReplyText());

        }

    }

    private String printList() {
        String out = "";
        for (Reply r : dataSet) out += r.getReplyText() + ", ";
        return out;
    }

}
