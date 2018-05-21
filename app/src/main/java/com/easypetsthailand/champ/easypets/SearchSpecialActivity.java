package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchSpecialActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = SearchSpecialActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        specialPetsGroup.setVisibility(View.GONE);

        searchSubmitButton.setOnClickListener(this);
        getBackIcon();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_submit){
            String keyword = keywordTextView.getText().toString();
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("keyword", keyword);
            startActivity(intent);
            finish();
        }
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

    @BindView(R.id.search_text)
    TextView keywordTextView;
    @BindView(R.id.search_submit)
    Button searchSubmitButton;
    @BindView(R.id.special_pets_group)
    ConstraintLayout specialPetsGroup;

}
