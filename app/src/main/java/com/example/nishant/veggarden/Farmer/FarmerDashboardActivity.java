package com.example.nishant.veggarden.Farmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishant.veggarden.Api;
import com.example.nishant.veggarden.MainActivity;
import com.example.nishant.veggarden.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FarmerDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView mName;
    CardView mLandDetails, mCropDetails, mRequests, mAgreements;
    OkHttpClient client;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLandDetails = findViewById(R.id.farmerLandDetails);
        mCropDetails = findViewById(R.id.farmerCropDetails);
        mRequests = findViewById(R.id.farmerRequests);
        mRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent request = new Intent(getApplicationContext(), FarmerRequestActivity.class);
                startActivity(request);
            }
        });
        mAgreements = findViewById(R.id.farmerAgreements);
        mName = findViewById(R.id.farmername);
        progressDialog = new ProgressDialog(this);
        
        checkUserExistance();

        mCropDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cropDetails = new Intent(getApplicationContext(), CropDetailsFarmerActivity.class);
                startActivity(cropDetails);
            }
        });

        mLandDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent landDetails = new Intent(getApplicationContext(), LandDetailsActivity.class);
                startActivity(landDetails);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddLand);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Logging out", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                FirebaseAuth.getInstance().signOut();
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkUserExistance() {

        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        client = new OkHttpClient();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/checkUserExistanceFarmer")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"uid\":\""+uid+"\"\n" +
                        "}"))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                        Toast.makeText(FarmerDashboardActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                        Intent auth = new Intent(getApplicationContext(), AuthFarmerActivity.class);
                        startActivity(auth);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                        try {
                            String json = response.body().string();
                            JSONObject mainObj = new JSONObject(json);
                            String result = mainObj.getString("result");
                            if (result.equals("success")) {
                                String name = mainObj.getString("name");
                                mName.setText(name);
                            } else if (result.equals("none")) {
                                Intent signup = new Intent(getApplicationContext(), FarmerSignupActivity.class);
                                startActivity(signup);
                            } else {
                                Intent auth = new Intent(getApplicationContext(), AuthFarmerActivity.class);
                                startActivity(auth);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.farmer_dashboard, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
