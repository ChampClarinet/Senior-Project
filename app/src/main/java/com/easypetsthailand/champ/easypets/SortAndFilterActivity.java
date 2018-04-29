package com.easypetsthailand.champ.easypets;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortAndFilterActivity extends AppCompatActivity {

    private final String TAG = SortAndFilterActivity.class.getSimpleName();
    private final int REQUEST_CODE_FILTER = 159;

    private String serviceType;
    private String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_and_filter);

        ButterKnife.bind(this);

        serviceType = getIntent().getStringExtra("type");
        sortBy = getIntent().getStringExtra("lastSortBy");

        changeSortByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortOptions();
            }
        });
        filterSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });

        getBackIcon();

        showFilter();

    }

    private void returnResult() {
        Intent intent = new Intent();
        intent.putExtra("sort_by", sortBy);
        //collect data from filter
        boolean isOpen = isOpenSwitch.isChecked();
        intent.putExtra("is_open", isOpen);
        if(serviceType.equalsIgnoreCase(getString(R.string.title_grooming))){
            int min = -1;
            int max = -1;
            String s = groomHotelMinTextView.getText().toString();
            if(s.length() > 0) min = Integer.parseInt(s);
            s = groomHotelMaxTextView.getText().toString();
            if(s.length() > 0) max = Integer.parseInt(s);
            intent.putExtra("groom_min_price", min);
            intent.putExtra("groom_max_price", max);
        }else if(serviceType.equalsIgnoreCase(getString(R.string.title_hotel))){
            boolean isAcceptOvernight = isAcceptedSwitch.isChecked();
            int min = -1;
            int max = -1;
            String s = groomHotelMinTextView.getText().toString();
            if(s.length() > 0) min = Integer.parseInt(s);
            s = groomHotelMaxTextView.getText().toString();
            if(s.length() > 0) max = Integer.parseInt(s);
            intent.putExtra("is_accept_overnight", isAcceptOvernight);
            intent.putExtra("hotel_min_price", min);
            intent.putExtra("hotel_max_price", max);
        }else if(serviceType.equalsIgnoreCase(getString(R.string.title_hospital))){
            boolean isAcceptOperation = isAcceptedSwitch.isChecked();
            int checkupMin = -1;
            int checkupMax = -1;
            int vaccineMin = -1;
            int vaccineMax = -1;
            int operationMin = -1;
            int operationMax = -1;
            String s = checkupMinTextView.getText().toString();
            if(s.length() > 0) checkupMin = Integer.parseInt(s);
            s = checkupMaxTextView.getText().toString();
            if(s.length() > 0) checkupMax = Integer.parseInt(s);
            s = vaccineMinTextView.getText().toString();
            if(s.length() > 0) vaccineMin = Integer.parseInt(s);
            s = vaccineMaxTextView.getText().toString();
            if(s.length() > 0) vaccineMax = Integer.parseInt(s);
            s = operationMinTextView.getText().toString();
            if(s.length() > 0) operationMin = Integer.parseInt(s);
            s = operationMaxTextView.getText().toString();
            if(s.length() > 0) operationMax = Integer.parseInt(s);
            intent.putExtra("is_accept_operation", isAcceptOperation);
            intent.putExtra("checkup_min_price", checkupMin);
            intent.putExtra("checkup_max_price", checkupMax);
            intent.putExtra("vaccine_min_price", vaccineMin);
            intent.putExtra("vaccine_max_price", vaccineMax);
            intent.putExtra("operation_min_price", operationMin);
            intent.putExtra("operation_max_price", operationMax);
        }
        setResult(REQUEST_CODE_FILTER, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSortLabel();
    }

    void setSortLabel(){
        String t = "unassigned";
        if (sortBy.equals(getString(R.string.distance_untranslated))) {
            t = getString(R.string.sort_distance);
        } else if (sortBy.equals(getString(R.string.name_untranslated))) {
            t = getString(R.string.sort_name);
        } else if (sortBy.equals(getString(R.string.popularity_untranslated))) {
            t = getString(R.string.sort_popularity);
        } else if (sortBy.equals(getString(R.string.hotel_price_untranslated)) || sortBy.equals(getString(R.string.groom_price_untranslated))) {
            t = getString(R.string.sort_price);
        } else if (sortBy.equals(getString(R.string.price_checkup_untranslated))) {
            t = getString(R.string.sort_price_checkup);
        } else if (sortBy.equals(getString(R.string.price_vaccine_untranslated))) {
            t = getString(R.string.sort_price_vaccine);
        } else if (sortBy.equals(getString(R.string.price_operation_untranslated))) {
            t = getString(R.string.sort_price_operation);
        }
        textSortBy.setText(t);
    }

    private void showFilter() {
        if(serviceType.equalsIgnoreCase(getString(R.string.title_grooming))){
            groomHotelPriceGroup.setVisibility(View.VISIBLE);
        }else if(serviceType.equalsIgnoreCase(getString(R.string.title_hotel))){
            groomHotelPriceGroup.setVisibility(View.VISIBLE);
            isAcceptedSwitch.setVisibility(View.VISIBLE);
            isAcceptedSwitch.setText(R.string.accept_overnight);
        }else if(serviceType.equalsIgnoreCase(getString(R.string.title_hospital))){
            isAcceptedSwitch.setVisibility(View.VISIBLE);
            isAcceptedSwitch.setText(R.string.accept_operation);
            hospitalPriceGroup.setVisibility(View.VISIBLE);
        }
    }

    private void showSortOptions() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort);
        builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if(serviceType.equalsIgnoreCase(getString(R.string.title_hospital))){
            String[] choices = {getString(R.string.sort_name), getString(R.string.sort_distance)
                    , getString(R.string.sort_price_checkup), getString(R.string.sort_price_vaccine)
                    , getString(R.string.sort_price_operation), getString(R.string.sort_popularity)};
            builder.setItems(choices, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            sortBy = getString(R.string.name_untranslated);
                            break;
                        case 1:
                            sortBy = getString(R.string.distance_untranslated);
                            break;
                        case 2:
                            sortBy = getString(R.string.price_checkup_untranslated);
                            break;
                        case 3:
                            sortBy = getString(R.string.price_vaccine_untranslated);
                            break;
                        case 4:
                            sortBy = getString(R.string.price_operation_untranslated);
                            break;
                        case 5:
                            sortBy = getString(R.string.popularity_untranslated);
                            break;
                        default:
                            sortBy = getString(R.string.distance_untranslated);
                            break;
                    }
                    onResume();
                    dialog.dismiss();
                }
            });
        }else{
            String[] choices = {getString(R.string.sort_name), getString(R.string.sort_distance)
                    , getString(R.string.sort_price), getString(R.string.sort_popularity)};
            builder.setItems(choices, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            sortBy = getString(R.string.name_untranslated);
                            break;
                        case 1:
                            sortBy = getString(R.string.distance_untranslated);
                            break;
                        case 2:
                            if(serviceType.equalsIgnoreCase(getString(R.string.title_hotel))) sortBy = getString(R.string.hotel_price_untranslated);
                            else sortBy = getString(R.string.groom_price_untranslated);
                            break;
                        case 3:
                            sortBy = getString(R.string.popularity_untranslated);
                            break;
                        default:
                            sortBy = getString(R.string.distance_untranslated);
                            break;
                    }
                    onResume();
                    dialog.dismiss();
                }
            });
        }
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBackIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @BindView(R.id.sort_by_attempt)
    TextView textSortBy;
    @BindView(R.id.sort_by_change_button)
    Button changeSortByButton;
    @BindView(R.id.filter_submit_button)
    Button filterSubmitButton;
    @BindView(R.id.switch_is_open)
    Switch isOpenSwitch;
    @BindView(R.id.switch_is_accepted)
    Switch isAcceptedSwitch;
    @BindView(R.id.groom_hotel_price_group)
    LinearLayout groomHotelPriceGroup;
    @BindView(R.id.groom_hotel_min_price)
    TextView groomHotelMinTextView;
    @BindView(R.id.groom_hotel_max_price)
    TextView groomHotelMaxTextView;
    @BindView(R.id.hospital_price_group)
    LinearLayout hospitalPriceGroup;
    @BindView(R.id.checkup_min_price)
    TextView checkupMinTextView;
    @BindView(R.id.checkup_max_price)
    TextView checkupMaxTextView;
    @BindView(R.id.vaccine_min_price)
    TextView vaccineMinTextView;
    @BindView(R.id.vaccine_max_price)
    TextView vaccineMaxTextView;
    @BindView(R.id.operation_min_price)
    TextView operationMinTextView;
    @BindView(R.id.operation_max_price)
    TextView operationMaxTextView;

}
