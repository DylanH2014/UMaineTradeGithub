package com.abhinav.dylan.umainetrade;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.R;

public class AddListings extends AppCompatActivity {

    private String itemName;
    private String itemPrice;
    private String itemCondition;
    private String itemCategory;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        toolbar.setTitle("Add New Listing");

        //Find UI elements

        final EditText itemNameET = (EditText) findViewById(R.id.ItemNameET);


        final EditText itemPriceET = (EditText) findViewById(R.id.ItemPriceET);



        Spinner categoryListSpinner  = (Spinner) findViewById(R.id.Spinner_Categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Categories_Array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        categoryListSpinner.setAdapter(adapter);
        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        setItemCategory("Electronics");
                        break;
                    case 1:
                        setItemCategory("Textbooks");
                        break;
                    case 2:
                        setItemCategory("Clothing");
                        break;
                    case 3:
                        setItemCategory("Furniture");
                        break;
                    case 4:
                        setItemCategory("Miscellaneous");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();

            }
        });


        Button addListingButton = (Button) findViewById(R.id.ListButton);
        addListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemName(itemNameET.getText().toString());
                setItemPrice(itemPriceET.getText().toString());

                Toast.makeText(getApplicationContext(), "Name: "+getItemName()+ "Price: "+ getItemPrice()+ "Condition: "+ getItemCondition()+ "Category" + getItemCategory(), Toast.LENGTH_SHORT).show();

            }
        });





    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.NewConditionRadio:
                if (checked)
                    setItemCondition("New");
                    break;
            case R.id.UsedConditionRadio:
                if (checked)
                    setItemCondition("Used");
                    break;
        }
    }




}
