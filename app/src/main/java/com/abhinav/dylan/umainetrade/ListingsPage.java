package com.abhinav.dylan.umainetrade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
//import com.abhinav.dylan.umainetrade.R;
import com.R;

import java.util.ArrayList;
import java.util.List;

import Data.Person;

public class ListingsPage extends AppCompatActivity {

    public String[] exampleForDrawer = {"Dire Straits", "Pink Floyd", "Led Zeppelin"};
    private DrawerLayout theDrawerLayout;
    private ListView drawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private List<Person> persons;
    private RecyclerView rv;
    public static Context listingContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listingContext = getApplicationContext();
        //setSupportActionBar(toolbar);
        toolbar.setTitle("Sale Page");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Do you want to create a new listing?", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Intent newListingIntent = new Intent(ListingsPage.this, AddListings.class);
                startActivity(newListingIntent);

            }
        });

        theDrawerLayout = (DrawerLayout) findViewById(R.id.navdrawerlayout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,theDrawerLayout, toolbar, R.string.EmailEditTextHint, R.string.app_name ){


        };

        theDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_drawer);


        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exampleForDrawer));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) parent.getItemAtPosition(position);
                Toast.makeText(ListingsPage.this, value, Toast.LENGTH_SHORT).show();

            }
        });


        //makeLists();

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);


        LinearLayoutManager llm = new LinearLayoutManager(ListingsPage.this);
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter(getApplicationContext());







    }

    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new Person("Emma Wilson", "23 years old", R.mipmap.photo1));
        persons.add(new Person("Lavery Maiss", "25 years old", R.mipmap.photo1));
        persons.add(new Person("Lillie Watts", "35 years old", R.mipmap.photo1));
    }









    private void initializeAdapter(Context context){
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }





}
