package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    private final String TAG = ServiceActivity.class.getSimpleName();
    private final int REQUEST_CODE_SEARCH = 592;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordTextView.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("keyword", keyword);
                setResult(REQUEST_CODE_SEARCH, intent);
                finish();
            }
        });
        searchReptilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String keyword = keywordTextView.getText().toString();
                if(keyword.length() > 0)
                    intent.putExtra("keyword", keyword);
                intent.putExtra("animals", "reptiles");
                setResult(REQUEST_CODE_SEARCH, intent);
                finish();
            }
        });
        searchReptilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String keyword = keywordTextView.getText().toString();
                if(keyword.length() > 0)
                    intent.putExtra("keyword", keyword);
                intent.putExtra("animals", "reptiles");
                setResult(REQUEST_CODE_SEARCH, intent);
                finish();
            }
        });
        searchBirdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String keyword = keywordTextView.getText().toString();
                if(keyword.length() > 0)
                    intent.putExtra("keyword", keyword);
                intent.putExtra("animals", "birds");
                setResult(REQUEST_CODE_SEARCH, intent);
                finish();
            }
        });
        searchAquaticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String keyword = keywordTextView.getText().toString();
                if(keyword.length() > 0)
                    intent.putExtra("keyword", keyword);
                intent.putExtra("animals", "aquatics");
                setResult(REQUEST_CODE_SEARCH, intent);
                finish();
            }
        });
        getBackIcon();

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
    @BindView(R.id.button_special_pets_reptiles)
    Button searchReptilesButton;
    @BindView(R.id.button_special_pets_birds)
    Button searchBirdsButton;
    @BindView(R.id.button_special_pets_aquatics)
    Button searchAquaticsButton;

}
