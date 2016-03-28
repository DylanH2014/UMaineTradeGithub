
package com.abhinav.dylan.umainetrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText searchEditText = (EditText) findViewById(R.id.searchET);
        final Button searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKeyword = searchEditText.getText().toString();
                Intent tabbedIntent = new Intent(getApplicationContext(), TabbedListings.class);
                tabbedIntent.putExtra("activityID", "fromSearch");
                tabbedIntent.putExtra("searchKeyword", searchKeyword);
                startActivity(tabbedIntent);
                finish();
            }
        });


    }

}
