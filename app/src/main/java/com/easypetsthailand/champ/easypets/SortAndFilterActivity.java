package com.easypetsthailand.champ.easypets;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    }

    private void returnResult() {
        Intent intent = new Intent();
        intent.putExtra("sort_by", sortBy);
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

}
