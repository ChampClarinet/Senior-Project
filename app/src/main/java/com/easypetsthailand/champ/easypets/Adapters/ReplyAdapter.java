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

import com.bumptech.glide.Glide;
import com.easypetsthailand.champ.easypets.Model.Reply;
import com.easypetsthailand.champ.easypets.Model.Service;
import com.easypetsthailand.champ.easypets.Model.Store_oldClass;
import com.easypetsthailand.champ.easypets.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.GenericHolder> {

    private Service service;
    private ArrayList<Reply> dataSet;
    private Context context;

    public ReplyAdapter(ArrayList<Reply> dataSet, Service service, Context context) {
        this.dataSet = dataSet;
        this.service = service;
        this.context = context;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(v, service, context);
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
        @BindView(R.id.cv_post_card)
        CardView cardView;
        @BindView(R.id.tv_poster_name)
        TextView cv_name;
        @BindView(R.id.imgview_poster_pic)
        ImageView cv_replier_pic;
        @BindView(R.id.tv_post_time)
        TextView cv_replyTime;
        @BindView(R.id.tv_post_text)
        TextView cv_replyText;
        @BindView(R.id.img_post_image)
        ImageView replyImageView;
        private Context context;
        private Service service;

        public ViewHolder(View itemView, Service service, Context context) {
            super(itemView);
            this.context = context;
            this.service = service;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setViewData(final Reply reply) {
            String replierName = service.getName();
            String replierPicturePath = context.getString(R.string.icon_storage_ref) + service.getLogoPath();
            Log.d("path", replierPicturePath);

            //cv_name
            cv_name.setText(replierName);

            //user pic
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(replierPicturePath);
            Glide.with(context).using(new FirebaseImageLoader()).load(reference).into(cv_replier_pic);

            //time
            cv_replyTime.setText(reply.getTimeReplied());

            //reply text
            cv_replyText.setText(reply.getReplyText());

            //reply image
            Log.d("replyPic", reply.getReplyPicturePath());
            if(!reply.getReplyPicturePath().equalsIgnoreCase("null")){
                String imagePath = context.getString(R.string.reply_images_storage_ref) + reply.getReplyPicturePath();
                StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(imagePath);
                Glide.with(context).using(new FirebaseImageLoader()).load(imgRef).centerCrop()
                        .placeholder(android.R.drawable.ic_menu_report_image).into(replyImageView);
                replyImageView.setVisibility(View.VISIBLE);
            }

        }

    }

    private String printList() {
        String out = "";
        for (Reply r : dataSet) out += r.getReplyText() + ", ";
        return out;
    }

}
