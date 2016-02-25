package com.abhinav.dylan.umainetrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.R;

public class ItemSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView itemNameTV = (TextView) findViewById(R.id.itemSummaryName);
        final TextView itemPriceTV = (TextView) findViewById(R.id.itemSummaryPrice);

        final TextView itemConditionTV = (TextView) findViewById(R.id.itemSummaryCondition);
        final TextView itemCategoryTV = (TextView) findViewById(R.id.itemSummaryCategory);
        final TextView itemDescriptionTV = (TextView) findViewById(R.id.itemSummaryDescription);







        Intent intent = getIntent();
        final String itemName = intent.getStringExtra("itemName");
        final String itemPrice = intent.getStringExtra("itemPrice");
        final String itemCondition = intent.getStringExtra("itemCondition");
        final String itemCategory = intent.getStringExtra("itemCategory");
        final String itemDescription = intent.getStringExtra("itemDescription");


        itemNameTV.setText(itemName);
        itemPriceTV.setText("$"+itemPrice);
        itemConditionTV.setText(itemCondition);
        itemCategoryTV.setText(itemCategory);
        itemDescriptionTV.setText(itemDescription);


    }

}
