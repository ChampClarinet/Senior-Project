package com.easypetsthailand.champ.easypets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easypetsthailand.champ.easypets.Model.Store;
import com.easypetsthailand.champ.easypets.R;
import com.easypetsthailand.champ.easypets.StoreActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.Utils.calculateDistance;
import static com.easypetsthailand.champ.easypets.Core.Utils.createLoadDialog;
import static com.easypetsthailand.champ.easypets.Core.Utils.isOpening;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.GenericHolder> {

    private ArrayList<Store> dataSet;
    private Context context;

    public ResultAdapter(ArrayList<Store> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_store, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(GenericHolder holder, int position) {
        Store store = dataSet.get(position);
        holder.setViewData(store);
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

        abstract public void setViewData(Store store);

    }

    public static class ViewHolder extends GenericHolder {

        //bind cardview's widgets
        @BindView(R.id.cv_store_card)
        CardView cardView;
        @BindView(R.id.tv_store_name)
        TextView cv_name;
        @BindView(R.id.tv_likes_count)
        TextView cv_likes;
        @BindView(R.id.imgview_store_picture)
        ImageView cv_logo;
        @BindView(R.id.tv_price_rate)
        TextView cv_rate;
        @BindView(R.id.tv_open)
        TextView cv_openLabel;
        @BindView(R.id.tv_close)
        TextView cv_closeLabel;
        @BindView(R.id.tv_open_time)
        TextView cv_openTimeTextView;
        @BindView(R.id.tv_distance)
        TextView cv_distance;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        private void openStoreActivity(Store store) throws Exception {
            Intent i = new Intent(context, StoreActivity.class);
            i.putExtra(context.getString(R.string.model_name_store), store);
            createLoadDialog(context, "loading");
            context.startActivity(i);
        }

        @Override
        public void setViewData(final Store store) {
            //logo
            String logoPath = context.getString(R.string.icon_storage_ref) + store.getLogoPath();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(logoPath);
            Glide.with(context).using(new FirebaseImageLoader()).load(reference).into(this.cv_logo);

            //cv_name
            this.cv_name.setText(store.getName());

            //cv_likes
            String s = Integer.toString(store.getLikes());
            this.cv_likes.setText(s);

            //price cv_rate
            String rateTextArgs = "";
            for (int i = 0; i < store.getPriceRate(); ++i) {
                rateTextArgs += context.getString(R.string.rate_symbol);
            }
            String rateText = context.getString(R.string.price_rate_label, rateTextArgs);
            cv_rate.setText(rateText);

            //openTime
            String openTime = store.getOpenTime();
            String closeTime = store.getCloseTime();
            boolean isOpening = isOpening(openTime, closeTime);
            cv_openLabel.setTextColor(Color.GREEN);
            cv_closeLabel.setTextColor(Color.RED);
            if (isOpening) {
                cv_openLabel.setVisibility(View.VISIBLE);
                cv_closeLabel.setVisibility(View.GONE);
            } else {
                cv_openLabel.setVisibility(View.GONE);
                cv_closeLabel.setVisibility(View.VISIBLE);
            }
            if (openTime != null && closeTime != null)
                cv_openTimeTextView.setText(context.getString(R.string.time_open_label, openTime, closeTime));
            else cv_openTimeTextView.setText(context.getString(R.string.all_day_open));

            //distance
            double distance = calculateDistance(store.getLatitude(), store.getLongitude());
            DecimalFormat format = new DecimalFormat("##.##");
            String distanceString = format.format(distance);
            if (distance > 1000) {
                distance /= 1000;
                distanceString = format.format(distance) + "k";
            }
            cv_distance.setText(context.getString(R.string.distance_label, distanceString));

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        openStoreActivity(store);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private String printList() {
        String out = "";
        for (Store s : dataSet) out += s.getName() + ", ";
        return out;
    }

}
