package com.easypetsthailand.champ.easypets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.easypetsthailand.champ.easypets.Adapters.ServiceCardAdapter;
import com.easypetsthailand.champ.easypets.Model.Service.Service;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MenuActivity extends AppCompatActivity {

    private final String TAG = MenuActivity.class.getSimpleName();

    @BindView(R.id.fab_sort_by)
    FloatingTextButton fabSortBy;
    @BindView(R.id.menu_activity_rv)
    RecyclerView RVMenu;
    @BindView(R.id.menu_activity_label_zero_results)
    TextView tvZeroResults;
    @BindView(R.id.menu_swipe_layout)
    SwipeRefreshLayout swipeLayout;

    private String sortBy;
    private ArrayList<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);

        String type = getIntent().getStringExtra("type");
        setTitle(type);

        sortBy = getString(R.string.distance);

        fabSortBy.setTitle(getString(R.string.sorted_by, sortBy));

        fabSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortOptions();
            }
        });

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh");
                swipeLayout.setRefreshing(true);
                refresh();
            }
        });

        //mock
        /*MockService m = new MockService();
        services = m.getMockService();
        */
        ServiceCardAdapter adapter = new ServiceCardAdapter(services, this);
        RVMenu.setLayoutManager(new LinearLayoutManager(this));
        RVMenu.setHasFixedSize(true);
        RVMenu.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh(){
        Log.d(TAG, "refresh");
        loadServices();
        if(services.size() > 0){
            RVMenu.setVisibility(View.VISIBLE);
            tvZeroResults.setVisibility(View.GONE);
        }else{
            RVMenu.setVisibility(View.GONE);
            tvZeroResults.setVisibility(View.VISIBLE);
        }
        swipeLayout.setRefreshing(false);
    }

    private void loadServices() {

    }

    //define sorting options dialog
    private void showSortOptions() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sort);
        builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        String[] choice = {getString(R.string.sort_name), getString(R.string.sort_distance)
                , getString(R.string.sort_price), getString(R.string.sort_popularity)};
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        sortBy = getString(R.string.sort_name);
                        break;
                    case 1:
                        sortBy = getString(R.string.sort_distance);
                        break;
                    case 2:
                        sortBy = getString(R.string.sort_price);
                        break;
                    case 3:
                        sortBy = getString(R.string.sort_popularity);
                        break;
                    default:
                        sortBy = getString(R.string.sort_distance);
                        break;
                }
                fabSortBy.setTitle(getString(R.string.sorted_by, sortBy));
                onResume();
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
