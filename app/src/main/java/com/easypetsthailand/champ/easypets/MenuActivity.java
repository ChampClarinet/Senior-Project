package com.easypetsthailand.champ.easypets;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.fab_sort_by)
    FloatingTextButton fabSortBy;

    private String sortBy;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        //query(text, type, filter, sortBy);
        //adapter.notifyDataSetChanged();
        //Log.d("sort by", sortBy);
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
