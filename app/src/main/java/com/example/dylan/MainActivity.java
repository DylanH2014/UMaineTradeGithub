package com.example.dylan;


        import java.io.BufferedInputStream;
        import java.io.BufferedReader;
        import java.io.ByteArrayOutputStream;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.URL;
        import java.net.URLConnection;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.StatusLine;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.R;
        import com.abhinav.dylan.umainetrade.AddListings;
        import com.google.zxing.IntentIntegrator;
        import com.google.zxing.IntentResult;
/**
 *Dylan H.
 *
 */
public class MainActivity extends Activity implements OnClickListener {

    //scan, addToListing, link, wrongBook buttons
    private Button scanBtn, previewBtn, linkBtn, wrongBook;
    //author, title, description, date and rating count text views
    private TextView authorText, titleText, descriptionText, dateText, ratingCountText, verifyBook;
    //layout for star rating
    private LinearLayout starLayout;
    //thumbnail image
    private ImageView thumbView;
    //star views
    private ImageView[] starViews;
    //thumbnail bitmap
    private Bitmap thumbImg;

    private String listingTitle, listingAuthor, listingDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //retrieve scan button and listen for clicks
        scanBtn = (Button)findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(this);



        //addToListing, link, yes, no buttons
        previewBtn = (Button)findViewById(R.id.preview_btn);
        previewBtn.setVisibility(View.GONE);
        previewBtn.setOnClickListener(this);
        linkBtn = (Button)findViewById(R.id.link_btn);
        linkBtn.setVisibility(View.GONE);
        linkBtn.setOnClickListener(this);

        wrongBook=(Button)findViewById(R.id.wrongBook);
        wrongBook.setVisibility(View.GONE);
        wrongBook.setOnClickListener(this);

        verifyBook=(TextView)findViewById(R.id.verification);



        //UI items
        authorText = (TextView)findViewById(R.id.book_author);
        titleText = (TextView)findViewById(R.id.book_title);
        descriptionText = (TextView)findViewById(R.id.book_description);
        dateText = (TextView)findViewById(R.id.book_date);
        starLayout = (LinearLayout)findViewById(R.id.star_layout);
        ratingCountText = (TextView)findViewById(R.id.book_rating_count);
        thumbView = (ImageView)findViewById(R.id.thumb);



        //star views
        starViews=new ImageView[5];
        for(int s=0; s<starViews.length; s++){
            starViews[s]=new ImageView(this);
        }

        //retrieve state
        if (savedInstanceState != null){
            authorText.setText(savedInstanceState.getString("author"));
            titleText.setText(savedInstanceState.getString("title"));
            descriptionText.setText(savedInstanceState.getString("description"));
            dateText.setText(savedInstanceState.getString("date"));
            ratingCountText.setText(savedInstanceState.getString("ratings"));


            //listingDesc = savedInstanceState.getString("description");
            Toast.makeText(MainActivity.this, listingAuthor+listingDesc+listingTitle, Toast.LENGTH_SHORT).show();



            int numStars = savedInstanceState.getInt("stars");//zero if null
            for(int s=0; s<numStars; s++){
                starViews[s].setImageResource(R.drawable.star);
                starLayout.addView(starViews[s]);
            }
            starLayout.setTag(numStars);
            thumbImg = (Bitmap)savedInstanceState.getParcelable("thumbPic");
            thumbView.setImageBitmap(thumbImg);
            previewBtn.setTag(savedInstanceState.getString("isbn"));//null if not there

            if(savedInstanceState.getBoolean("isEmbed")) previewBtn.setEnabled(true);
            else previewBtn.setEnabled(false);
            if(savedInstanceState.getInt("isLink")==View.VISIBLE) linkBtn.setVisibility(View.VISIBLE);
            else linkBtn.setVisibility(View.GONE);
            previewBtn.setVisibility(View.VISIBLE);

        }
    }

    public void onClick(View v){

        //check for scan button
        if(v.getId()==R.id.scan_button){
            //instantiate ZXing integration class
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            //start scanning
            scanIntegrator.initiateScan();
        }
        //link to google books info for scanned textbook
        else if(v.getId()==R.id.link_btn){
            //get the url tag
            String tag = (String)v.getTag();
            //launch the url
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(tag));
            startActivity(webIntent);
        }
        //use intent to send textbook information to AddListings class
        else if(v.getId()==R.id.preview_btn){
            String tag = (String)v.getTag();

            //push textbook info to AddListings class
            Intent intent = new Intent(this, AddListings.class);
            intent.putExtra("isbn", tag);
            intent.putExtra("activityID", "fromMainActivity");
            intent.putExtra("title", titleText.getText().toString() );
            intent.putExtra("author", authorText.getText().toString());
            intent.putExtra("description", descriptionText.getText().toString());

            //send the thumbnail image to addListings class
            Bitmap _bitmap = thumbImg;
            ByteArrayOutputStream _bs = new ByteArrayOutputStream();
            _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
            intent.putExtra("byteArray", _bs.toByteArray());


            //begin the intent
            startActivity(intent);

        }
        //use intent to open AddListings class for manual item creation
        else if(v.getId()==R.id.wrongBook){
            Intent intent = new Intent(this, AddListings.class);
            intent.putExtra("activityID", "fromMainActivity2");
            startActivity(intent);
        }



    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve result of scanning - instantiate ZXing object
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //check for a valid result
        if (scanningResult != null) {
            //get content from Intent Result
            String scanContent = scanningResult.getContents();
            //get format name of data scanned
            String scanFormat = scanningResult.getFormatName();
            Log.v("SCAN", "content: "+scanContent+" - format: "+scanFormat);
            if(scanContent!=null && scanFormat!=null && scanFormat.equalsIgnoreCase("EAN_13")){
                previewBtn.setTag(scanContent);
                //google books API
                String key = "AIzaSyD1oZ-hjgz-B79j6uj4IR63q8d3PSyRa7o";
                String bookSearchString = "https://www.googleapis.com/books/v1/volumes?" +
                        "q=isbn:"+scanContent+"&key=" + key;
                //fetch search results
                new GetBookInfo().execute(bookSearchString);
            }
            else{
                //not ean format
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Not a valid scan!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            //invalid scan data or scan canceled
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No book scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //class to fetch book information
    private class GetBookInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... bookURLs) {
            StringBuilder bookBuilder = new StringBuilder();
            for (String bookSearchURL : bookURLs) {
                HttpClient bookClient = new DefaultHttpClient();
                try {
                    //get the data
                    HttpGet bookGet = new HttpGet(bookSearchURL);
                    HttpResponse bookResponse = bookClient.execute(bookGet);
                    StatusLine bookSearchStatus = bookResponse.getStatusLine();
                    if (bookSearchStatus.getStatusCode()==200) {
                        //we have a result
                        HttpEntity bookEntity = bookResponse.getEntity();
                        InputStream bookContent = bookEntity.getContent();
                        InputStreamReader bookInput = new InputStreamReader(bookContent);
                        BufferedReader bookReader = new BufferedReader(bookInput);
                        String lineIn;
                        while ((lineIn=bookReader.readLine())!=null) {
                            bookBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            return bookBuilder.toString();
        }
        protected void onPostExecute(String result) {
            try{
                //parse results
                scanBtn.setEnabled(false);
                verifyBook.setVisibility(View.VISIBLE);
                previewBtn.setVisibility(View.VISIBLE);
                wrongBook.setVisibility(View.VISIBLE);
                JSONObject resultObject = new JSONObject(result);
                JSONArray bookArray = resultObject.getJSONArray("items");
                JSONObject bookObject = bookArray.getJSONObject(0);
                JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");
                //set title
                try{
                    titleText.setText("TITLE : "+volumeObject.getString("title"));
                }
                catch(JSONException jse){
                    titleText.setText("");
                    jse.printStackTrace();
                }
                //set author(s) - can have multiple
                StringBuilder authorBuild = new StringBuilder("");
                try{
                    JSONArray authorArray = volumeObject.getJSONArray("authors");
                    for(int a=0; a<authorArray.length(); a++){
                        if(a>0) authorBuild.append(", ");
                        authorBuild.append(authorArray.getString(a));
                    }
                    authorText.setText("AUTHOR(S): "+authorBuild.toString());

                }
                catch(JSONException jse){
                    authorText.setText("");
                    jse.printStackTrace();
                }
                //set publication date
                try{ dateText.setText("PUBLISHED: "+volumeObject.getString("publishedDate"));
                }
                catch(JSONException jse){
                    dateText.setText("");
                    jse.printStackTrace();
                }
                //set book description
                try{
                    descriptionText.setText("DESCRIPTION: " + volumeObject.getString("description"));

                }
                catch(JSONException jse){
                    descriptionText.setText("");
                    jse.printStackTrace();
                }
                //set star rating display
                try{
                    double decNumStars = Double.parseDouble(volumeObject.getString("averageRating"));
                    int numStars = (int)decNumStars;
                    starLayout.setTag(numStars);
                    starLayout.removeAllViews();
                    for(int s=0; s<numStars; s++){
                        starViews[s].setImageResource(R.drawable.star);
                        starLayout.addView(starViews[s]);
                    }
                }
                catch(JSONException jse){
                    starLayout.removeAllViews();
                    jse.printStackTrace();
                }
                //set rating count
                try{ ratingCountText.setText(" - "+volumeObject.getString("ratingsCount")+" ratings"); }
                catch(JSONException jse){
                    ratingCountText.setText("");
                    jse.printStackTrace();
                }
                //preview availability
                try{
                    boolean isEmbeddable = Boolean.parseBoolean
                            (bookObject.getJSONObject("accessInfo").getString("embeddable"));
                    previewBtn.setEnabled(true);

                }
                catch(JSONException jse){
                    previewBtn.setEnabled(false);
                    jse.printStackTrace();
                }
                //enable wrongBook button
                wrongBook.setEnabled(true);

                //set link button to visible
                try{
                    linkBtn.setTag(volumeObject.getString("infoLink"));
                    linkBtn.setVisibility(View.VISIBLE);
                }
                catch(JSONException jse){
                    linkBtn.setVisibility(View.GONE);
                    jse.printStackTrace();
                }
                //image thumbnail
                try{
                    JSONObject imageInfo = volumeObject.getJSONObject("imageLinks");
                    new GetBookThumb().execute(imageInfo.getString("smallThumbnail"));
                }
                catch(JSONException jse){
                    thumbView.setImageBitmap(null);
                    jse.printStackTrace();
                }
            }
            catch (Exception e) {
                //no result
                e.printStackTrace();
                titleText.setText("NOT FOUND");
                authorText.setText("");
                descriptionText.setText("");
                dateText.setText("");
                starLayout.removeAllViews();
                ratingCountText.setText("");
                thumbView.setImageBitmap(null);
                previewBtn.setVisibility(View.GONE);
                wrongBook.setVisibility(View.GONE);
            }
        }
    }

    //class to fetch thumbnail image
    private class GetBookThumb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... thumbURLs) {
            try{
                //attempt to download thumbnail image using passed URL
                URL thumbURL = new URL(thumbURLs[0]);
                URLConnection thumbConn = thumbURL.openConnection();
                thumbConn.connect();
                InputStream thumbIn = thumbConn.getInputStream();
                BufferedInputStream thumbBuff = new BufferedInputStream(thumbIn);
                thumbImg = BitmapFactory.decodeStream(thumbBuff);

                thumbBuff.close();
                thumbIn.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return "";
        }
        protected void onPostExecute(String result) {
            //show the thumbnail image
            thumbView.setImageBitmap(thumbImg);
        }
    }
    //save state
    protected void onSaveInstanceState(Bundle savedBundle) {
        savedBundle.putString("title", ""+titleText.getText());
        savedBundle.putString("author", ""+authorText.getText());
        savedBundle.putString("description", ""+descriptionText.getText());
        savedBundle.putString("date", ""+dateText.getText());
        savedBundle.putString("ratings", "" + ratingCountText.getText());
        savedBundle.putParcelable("thumbPic", thumbImg);
        if(starLayout.getTag()!=null)
            savedBundle.putInt("stars", Integer.parseInt(starLayout.getTag().toString()));
        savedBundle.putBoolean("isEmbed", previewBtn.isEnabled());
        savedBundle.putBoolean("incorrect", wrongBook.isEnabled());
        savedBundle.putInt("isLink", linkBtn.getVisibility());
        if(previewBtn.getTag()!=null)
            savedBundle.putString("isbn", previewBtn.getTag().toString());
    }

}