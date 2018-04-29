package com.easypetsthailand.champ.easypets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = ServiceActivity.class.getSimpleName();
    private final int REQUEST_CODE_SEARCH = 592;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchSubmitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_submit){
            String keyword = keywordTextView.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("keyword", keyword);
            setResult(REQUEST_CODE_SEARCH, intent);
            finish();
        }
    }

    @BindView(R.id.search_text)
    TextView keywordTextView;
    @BindView(R.id.search_submit)
    Button searchSubmitButton;

}
