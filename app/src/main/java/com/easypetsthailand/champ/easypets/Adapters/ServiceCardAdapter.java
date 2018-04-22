package com.easypetsthailand.champ.easypets.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.easypetsthailand.champ.easypets.Core.Utils;
import com.easypetsthailand.champ.easypets.Model.Service;
import com.easypetsthailand.champ.easypets.R;
import com.easypetsthailand.champ.easypets.ServiceActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.easypetsthailand.champ.easypets.Core.Utils.calculateDistance;
import static com.easypetsthailand.champ.easypets.Core.Utils.createLoadDialog;
import static com.easypetsthailand.champ.easypets.Core.Utils.isOpening;

public class ServiceCardAdapter extends RecyclerView.Adapter<ServiceCardAdapter.GenericHolder> {

    private ArrayList<Service> dataSet;
    private Context context;

    public ServiceCardAdapter(ArrayList<Service> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_service, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(GenericHolder holder, int position) {
        Service service = dataSet.get(position);
        holder.setViewData(service);
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

        abstract public void setViewData(Service service);

    }

    public static class ViewHolder extends GenericHolder {

        //bind cardview's widgets
        @BindView(R.id.cv_store_card)
        CardView cardView;
        @BindView(R.id.tv_service_name)
        TextView cv_name;
        @BindView(R.id.tv_service_likes_count)
        TextView cv_likes;
        @BindView(R.id.imgview_service_picture)
        ImageView cv_logo;
        @BindView(R.id.tv_service_depositable)
        TextView cv_depositable;
        @BindView(R.id.tv_service_open)
        TextView cv_openLabel;
        @BindView(R.id.tv_service_close)
        TextView cv_closeLabel;
        @BindView(R.id.tv_service_time)
        TextView cv_openTimeTextView;
        @BindView(R.id.tv_service_distance)
        TextView cv_distance;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        private void openStoreActivity(Service service) {
            Intent i = new Intent(context, ServiceActivity.class);
            i.putExtra(context.getString(R.string.model_name_service), service);
            createLoadDialog(context, context.getString(R.string.loading));
            context.startActivity(i);
        }

        @Override
        public void setViewData(final Service service) {
            //Log.d("adapter", "setting view for "+service.getName());
            //logo
            String logoPath = context.getString(R.string.icon_storage_ref) + service.getLogoPath();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(logoPath);
            Glide.with(context).using(new FirebaseImageLoader()).load(reference).centerCrop()
                    .placeholder(android.R.drawable.ic_menu_report_image).into(this.cv_logo);

            //cv_name
            this.cv_name.setText(service.getName());

            //cv_likes
            String s = Integer.toString(service.getLikes());
            this.cv_likes.setText(s);

            //cv_depositable
            setDepositable(service);

            //openTime
            boolean[] openDays = service.getOpenDays();
            String openTime = service.getOpenTime();
            String closeTime = service.getCloseTime();
            boolean isOpening = isOpening(openDays, openTime, closeTime);
            cv_openLabel.setTextColor(Color.GREEN);
            cv_closeLabel.setTextColor(Color.RED);
            String timeLabel = "";
            if (isOpening) {
                cv_openLabel.setVisibility(View.VISIBLE);
                cv_closeLabel.setVisibility(View.GONE);
            } else {
                cv_openLabel.setVisibility(View.GONE);
                cv_closeLabel.setVisibility(View.VISIBLE);
                timeLabel += Utils.getNextOpens(service, context);
            }
            if (openTime != null && closeTime != null)
                timeLabel += context.getString(R.string.time_open_label, openTime, closeTime);
            else timeLabel += context.getString(R.string.all_day_open);
            cv_openTimeTextView.setText(timeLabel);

            //distance
            double distance = calculateDistance(service.getLatitude(), service.getLongitude());
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
                        openStoreActivity(service);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        private void setDepositable(Service service) {
            String url = context.getString(R.string.URL) + context.getString(R.string.GET_HOTEL_BY_SERVICE_ID_URL, Integer.toString(service.getServiceId()));
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("404")) {
                        cv_depositable.setText(context.getString(R.string.deposit_unavailable));
                        return;
                    }
                    try {
                        JSONObject object = new JSONObject(response);
                        int isAcceptOvernight = object.getInt(context.getString(R.string.is_accept_overnight));
                        if (isAcceptOvernight == 1) {
                            String s = context.getString(R.string.deposit_available) + "\n" + context.getString(R.string.accept_overnight);
                            cv_depositable.setText(s);
                        }else{
                            cv_depositable.setText(context.getString(R.string.deposit_available));
                        }
                    } catch (JSONException e) {
                        Log.d("adapter.depositable", "json error");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("adapter.depositable", "error response");
                }
            });
            Volley.newRequestQueue(context).add(request);
        }
    }

    public String printList() {
        String out = "";
        for (Service s : dataSet) out += s.getName() + ", ";
        return out;
    }

}
