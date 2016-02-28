package com.abhinav.dylan.umainetrade;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import java.util.*;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.R;

import org.postgresql.core.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class AddListings extends AppCompatActivity {

    private String itemName;
    private int itemPrice;
    private int itemCondition;
    private int itemCategory;
    public static int ownerId;

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    private int photoId;

    public int getItemOwnerId() {
        return itemOwnerId;
    }

    public void setItemOwnerId(int itemOwnerId) {
        this.itemOwnerId = itemOwnerId;
    }

    private int itemOwnerId;


    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    private String itemDescription;
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(int itemCondition) {
        this.itemCondition = itemCondition;
    }

    public int getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
    }

    private String getAuthor, getTitle, getDescription;

    private Intent intent;
    private Bitmap mImageBitmap;
    private static final int CAMERA_REQUEST = 1888;

    public ImageView getAddImage() {
        return addImage;
    }

    public void setAddImage(ImageView addImage) {
        this.addImage = addImage;
    }

    private ImageView addImage;
    private Uri mImageCaptureUri;
    private String pathToImage;
    public static byte[] imageBytes;

    File destination;
    String imagePath;

    public String getFinalImagePath() {
        return finalImagePath;
    }

    public void setFinalImagePath(String finalImagePath) {
        this.finalImagePath = finalImagePath;
    }

    private String finalImagePath;



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
        final EditText itemDescriptionET = (EditText) findViewById(R.id.itemDescriptionET);
        addImage = (ImageView) findViewById(R.id.addImageIV);
        Button getPhoto = (Button) findViewById(R.id.GetPhotoButton);

        final Spinner categoryListSpinner  = (Spinner) findViewById(R.id.Spinner_Categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Categories_Array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        categoryListSpinner.setAdapter(adapter);
        categoryListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setItemCategory(1);
                        break;
                    case 1:
                        setItemCategory(2);
                        break;
                    case 2:
                        setItemCategory(3);
                        break;
                    case 3:
                        setItemCategory(4);
                        break;
                    case 4:
                        setItemCategory(5);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();

            }


        });

        getPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage.setImageResource(android.R.color.transparent);
                DBLogin login = new DBLogin();
                //byte[] imageByteArray = login.getImage(getFinalImagePath());
                //byte[] imageByteArray = login.getImage(getFinalImagePath());
                byte[] image = login.getImage(1);

                //Toast.makeText(AddListings.this, Arrays.toString(imageByteArray), Toast.LENGTH_SHORT).show();

                Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                addImage.setImageBitmap(bmp);


            }
        });

        Button clearImage = (Button) findViewById(R.id.ClearButton);
        clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage.setImageResource(android.R.color.transparent);
            }
        });


        Button addListingButton = (Button) findViewById(R.id.ListButton);
        addListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBLogin login = new DBLogin();
                try {
                    byte [] byteImage = ImageToByte(new File(getFinalImagePath()));
                    login.addImage(byteImage);
                    setPhotoId(login.getImageId(byteImage));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                setItemName(itemNameET.getText().toString());
                setItemPrice(Integer.parseInt(itemPriceET.getText().toString()));
                setItemOwnerId(ownerId);
                setItemDescription(itemDescriptionET.getText().toString());



                //File file = new File(getFinalImagePath());


                login.addListing(getItemName(), getItemPrice(), getItemCondition(), getPhotoId(), getItemCategory(), getItemOwnerId(), getItemDescription());

                //Toast.makeText(getApplicationContext(), "Name: " + getItemName() + "Price: " + getItemPrice() + "Condition: " + getItemCondition() + "Category" + getItemCategory(), Toast.LENGTH_SHORT).show();

            }
        });

        String name =   dateToString(new Date(),"yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");



        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));

                startActivityForResult(cameraIntent, CAMERA_REQUEST);


            }
        });


        intent = getIntent();

            if (intent != null && intent.hasExtra("activityID")) {
                String checkActivity = intent.getStringExtra("activityID");
                String tgetAuthor = intent.getStringExtra("author");
                String tgetTitle = intent.getStringExtra("title");
                String tgetDescription = intent.getStringExtra("description");
                //Toast.makeText(AddListings.this, tgetAuthor, Toast.LENGTH_SHORT).show();
                //Toast.makeText(AddListings.this, checkActivity, Toast.LENGTH_SHORT).show();

                if (checkActivity.equals("fromMainActivity")) {

                    itemNameET.setText(tgetTitle);
                    itemDescriptionET.setText("Author is: " + tgetAuthor + "Description: " + tgetDescription);
                    categoryListSpinner.setSelection(1);

                }


            }

        }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static byte [] ImageToByte(File file) throws FileNotFoundException{
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
        }
        byte[] bytes = bos.toByteArray();

        return bytes;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");

            //Uri sample = (Uri) data.getExtras().get("mImageCaptureUri");
            //Uri myUri = Uri.parse(data.getStringExtra("imageUri"));
            //Uri uri = intent.getParcelableExtra("imageUri");
            //addImage.setImageBitmap(photo);
            //pathToImage = mImageCaptureUri.getPath();

            try {
                FileInputStream in = new FileInputStream(destination);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                Bitmap bmp = BitmapFactory.decodeStream(in, null, options);
                addImage.setImageBitmap(bmp);
                imagePath = destination.getAbsolutePath();
                setFinalImagePath(imagePath);
                //Toast.makeText(AddListings.this, imagePath, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }




    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.NewConditionRadio:
                if (checked)
                    setItemCondition(1);
                    break;
            case R.id.UsedConditionRadio:
                if (checked)
                    setItemCondition(2);
                    break;
        }
    }




}
