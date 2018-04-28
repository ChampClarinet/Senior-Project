package com.easypetsthailand.champ.easypets.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easypetsthailand.champ.easypets.Model.OtherService;
import com.easypetsthailand.champ.easypets.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtherServiceAdapter extends RecyclerView.Adapter<OtherServiceAdapter.GenericHolder> {

    private Context context;
    private ArrayList<OtherService> dataSet;

    public OtherServiceAdapter(ArrayList<OtherService> dataSet, Context context) {
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_other_service, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(GenericHolder holder, int position) {
        OtherService service = dataSet.get(position);
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

        abstract public void setViewData(OtherService service);

    }

    public static class ViewHolder extends GenericHolder {

        //bind cardview's widgets
        @BindView(R.id.other_heading)
        TextView detail;
        @BindView(R.id.other_price)
        TextView price;
        Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setViewData(final OtherService service) {
            detail.setText(service.getDetail());
            if(service.getPrice() != null || !service.getPrice().equalsIgnoreCase("null")) price.setText(service.getPrice());
            else price.setText(context.getString(R.string.vary));
        }

    }

    public String printList() {
        String out = "";
        for (OtherService s : dataSet) out += s.getDetail() + ", ";
        return out;
    }

}
