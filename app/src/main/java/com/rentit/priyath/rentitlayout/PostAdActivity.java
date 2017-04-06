package com.rentit.priyath.rentitlayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/*TODO

has to call the location activity to get location then add using that location to post the data
save the data collected to a class object and save it in storage using file stream

*/
public class PostAdActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText subinfo1;
    EditText subinfo2;
    EditText subinfo3;
    EditText subinfo4;
    EditText subinfo5;
    EditText title;

    ImageButton AdImage1;
    ImageButton AdImage2;
    ImageButton AdImage3;
    ImageButton AdImage4;
    ImageButton AdImage5;

    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;
    ProgressBar progressBar4;
    ProgressBar progressBar5;

    Button button;

    ArrayList<String> imageNames;

    private int PICK_IMAGE_REQUEST = 1;

    int count =0;
    int type = 0;
    SimpleDateFormat sdf;
    String url= "http://rentitapi.herokuapp.com/ad_image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.postAds);

        imageNames = new ArrayList<>();
        AdImage1 = (ImageButton)findViewById(R.id.AdImage1);
        AdImage2 = (ImageButton)findViewById(R.id.AdImage2);
        AdImage3 = (ImageButton)findViewById(R.id.AdImage3);
        AdImage4 = (ImageButton)findViewById(R.id.AdImage4);
        AdImage5 = (ImageButton)findViewById(R.id.AdImage5);


        subinfo1 = (EditText)findViewById(R.id.subinfo1);
        subinfo2 = (EditText)findViewById(R.id.subinfo2);
        subinfo3 = (EditText)findViewById(R.id.subinfo3);
        subinfo4 = (EditText)findViewById(R.id.subinfo4);
        subinfo5 = (EditText)findViewById(R.id.subinfo5);
        title = (EditText)findViewById(R.id.title);

        progressBar1= (ProgressBar)findViewById(R.id.progressBar1);
        progressBar2= (ProgressBar)findViewById(R.id.progressBar2);
        progressBar3= (ProgressBar)findViewById(R.id.progressBar3);
        progressBar4= (ProgressBar)findViewById(R.id.progressBar4);
        progressBar5= (ProgressBar)findViewById(R.id.progressBar5);

        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Spinner categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
                if(position == 0){//House
                    subinfo2.setHint("Area with units");
                    subinfo3.setHint("Bedrooms");
                    subinfo4.setHint("Bathrooms");
                }else if(position == 1){//car
                    subinfo2.setHint("Brand");
                    subinfo3.setHint("Model");
                    subinfo4.setHint("Year of Manufacture");
                }else if(position == 2){//heavy
                    subinfo2.setHint("Number of seats");
                    subinfo3.setHint("Number of wheels");
                    subinfo4.setHint("Year of Manufacture");
                }else if(position == 3){//Bikes
                    subinfo2.setHint("Brand");
                    subinfo3.setHint("Model");
                    subinfo4.setHint("Year of Manufacture");
                }else if(position == 4){//commercials
                    subinfo2.setHint("Area with unit");
                    subinfo3.setHint("Parking Capacity");
                    subinfo4.setHint("Year of Construction");
                }else if(position == 5){//auditoriums
                    subinfo2.setHint("Seating Capacity");
                    subinfo3.setHint("Parking Capacity");
                    subinfo4.setHint("Area");
                }else if(position == 6){//sound
                    subinfo2.setHint("Number of speakers");
                    subinfo3.setHint("Power Specification");
                    subinfo4.setVisibility(View.INVISIBLE);
                }else if(position == 7){
                    subinfo2.setHint("Type");
                    subinfo3.setHint("Power Specification");
                    subinfo4.setVisibility(View.INVISIBLE);
                }else if(position == 8){
                    subinfo2.setHint("Type");
                    subinfo3.setVisibility(View.INVISIBLE);
                    subinfo4.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AdImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),1);


            }
        });
        AdImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),2);

            }
        });
        AdImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),3);

            }
        });
        AdImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),4);

            }
        });
        AdImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"),5);
            }
        });


        button = (Button)findViewById(R.id.locationSpecification);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkdataEntered()){
                    Intent intent = new Intent(PostAdActivity.this,location.class);
                    intent.putExtra("flag",1);
                    intent.putExtra("Title",title.getText().toString());
                    intent.putExtra("Rent",Integer.parseInt(subinfo1.getText().toString()));
                    intent.putExtra("Description",subinfo5.getText().toString());
                    intent.putExtra("subitem1",subinfo2.getText().toString());
                    intent.putExtra("subitem2",subinfo3.getText().toString());
                    intent.putExtra("subitem3",subinfo4.getText().toString());
                    intent.putExtra("Type",type);
                    intent.putExtra("image_1",imageNames.get(0));
                    intent.putExtra("image_2",imageNames.get(1));
                    intent.putExtra("image_3",imageNames.get(2));
                    intent.putExtra("image_4",imageNames.get(3));
                    intent.putExtra("image_5",imageNames.get(4));
                    intent.putExtra("AverageRating",0);
                    intent.putExtra("OwnerDetails","fdfafadfdafasdf");



                    startActivity(intent);
                }else{
                    Toast.makeText(PostAdActivity.this, "Enter all the details and try again", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void saveDataEntered() throws JSONException, IOException {
        JSONObject postData = new JSONObject();
        postData.put("Title",title.getText().toString());
        postData.put("Rent",subinfo1.getText());
        postData.put("subitem1",subinfo2.getText());
        postData.put("subitem2",subinfo3.getText());
        postData.put("subitem2",subinfo4.getText());
        postData.put("Description",subinfo5.getText());
        //postData.put("image_1",imageNames.get(0));
        //postData.put("image_2",imageNames.get(1));
        //postData.put("image_3",imageNames.get(2));
        //postData.put("image_4",imageNames.get(3));
        //postData.put("image_5",imageNames.get(4));
        postData.put("type",type);
        ;
        //FileOutputStream out = this.openFileOutput("postData",MODE_PRIVATE);
        //ObjectOutputStream oos = new ObjectOutputStream(out);
        //oos.writeObject(ad);
        //oos.close();
        //out.close();
    }

    public boolean checkdataEntered(){
        if(title.getText().length()==0)
            return false;
        if(subinfo1.getText().length()==0)
            return false;
        if(subinfo2.getText().length()==0)
            return false;
        if(subinfo3.getText().length()==0)
            return false;
        if(type < 6) {
            if (subinfo4.getText().length() == 0)
                return false;
            if (subinfo5.getText().length() == 0)
                return false;

        }
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.MyAds) {
            Intent intent = new Intent(this,myAds.class);
            startActivity(intent);

        } else if (id == R.id.postAds) {

        } else if (id == R.id.Wishlist) {
            Intent intent = new Intent(this,wishlist.class);
            startActivity(intent);

        } else if (id == R.id.location) {
            Intent intent = new Intent(this,location.class);
            intent.putExtra("flag",2);
            startActivity(intent);

        } else if (id == R.id.History) {
            Intent intent =  new Intent(this,history.class);
            startActivity(intent);

        }else if (id == R.id.chat) {
            Intent intent =  new Intent(this,chatActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    Bitmap[] bitmap = new  Bitmap[6];
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode < 6 && requestCode >0 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri uri = data.getData();
            try{
                bitmap[requestCode-1] = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                if(requestCode == 1){
                    AdImage1.setImageBitmap(bitmap[requestCode-1]);
                    progressBar1.setVisibility(View.VISIBLE);
                    progressBar1.setProgress(0);
                }else if(requestCode == 2){
                    AdImage2.setImageBitmap(bitmap[requestCode-1]);
                    progressBar2.setVisibility(View.VISIBLE);
                    progressBar2.setProgress(0);
                }else if( requestCode == 3){
                    AdImage3.setImageBitmap(bitmap[requestCode-1]);
                    progressBar3.setVisibility(View.VISIBLE);
                    progressBar3.setProgress(0);
                }else if(requestCode == 4){
                    AdImage4.setImageBitmap(bitmap[requestCode-1]);
                    progressBar4.setVisibility(View.VISIBLE);
                    progressBar4.setProgress(0);
                }else if(requestCode == 5){
                    AdImage5.setImageBitmap(bitmap[requestCode-1]);
                    progressBar5.setVisibility(View.VISIBLE);
                    progressBar5.setProgress(0);
                }

                SendHttpRequestTask t = new SendHttpRequestTask();
                t.execute(requestCode);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SendHttpRequestTask extends AsyncTask<Integer, Void, Integer> {


        @Override
        protected Integer doInBackground(Integer... params) {
            Bitmap b = bitmap[params[0]-1];
            String imagename = "priyathsaji"+sdf.format(new Date())+".png";
            imageNames.add(imagename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 0, baos);

            try {
                com.rentit.priyath.rentitlayout.HttpClient client = new com.rentit.priyath.rentitlayout.HttpClient(url);
                client.connectForMultipart();
                client.addFilePart("file",imagename, baos.toByteArray());
                client.finishMultipart();
                String data = client.getResponse();
            } catch (Throwable t) {
                t.printStackTrace();
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(Integer requestCode) {
            if(requestCode == 1){
                AdImage1.setImageBitmap(bitmap[requestCode-1]);
                progressBar1.setVisibility(View.INVISIBLE);
                progressBar1.setProgress(0);
            }else if(requestCode == 2){
                AdImage2.setImageBitmap(bitmap[requestCode-1]);
                progressBar2.setVisibility(View.INVISIBLE);
                progressBar2.setProgress(0);
            }else if( requestCode == 3){
                AdImage3.setImageBitmap(bitmap[requestCode-1]);
                progressBar3.setVisibility(View.INVISIBLE);
                progressBar3.setProgress(0);
            }else if(requestCode == 4){
                AdImage4.setImageBitmap(bitmap[requestCode-1]);
                progressBar4.setVisibility(View.INVISIBLE);
                progressBar4.setProgress(0);
            }else if(requestCode == 5){
                AdImage5.setImageBitmap(bitmap[requestCode-1]);
                progressBar5.setVisibility(View.INVISIBLE);
                progressBar5.setProgress(0);
            }

        }
    }

}
