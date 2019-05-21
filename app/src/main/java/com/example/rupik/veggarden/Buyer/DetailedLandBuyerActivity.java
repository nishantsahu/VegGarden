package com.example.rupik.veggarden.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rupik.veggarden.Farmer.Adapter.CropDetailsAdapterLand;
import com.example.rupik.veggarden.Api;
import com.example.rupik.veggarden.Data.Crops;
import com.example.rupik.veggarden.Farmer.AddCropActivity;
import com.example.rupik.veggarden.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DetailedLandBuyerActivity extends AppCompatActivity {

    TextView mLandName, mLandArea, mSoilType, mLandAddress;
    RecyclerView mCropDetails;
    OkHttpClient client;
    String cropId, cropName, cropPrice, cropQuantity;
    CropDetailsAdapterLand adapter;
    List<Crops> cropsList;
    ProgressDialog progressDialog;
    Intent addCrop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_land_buyer);

        mLandName = findViewById(R.id.cropDetailFarmerLandName);
        mLandArea = findViewById(R.id.cropDetailFarmerLandArea);
        mSoilType = findViewById(R.id.cropDetailFarmerSoilType);
        mLandAddress = findViewById(R.id.cropDetailFarmerLandAddress);
        mCropDetails = findViewById(R.id.cropDetailFarmerUserLand);

        client = new OkHttpClient();

        mCropDetails.setHasFixedSize(true);
        mCropDetails.setLayoutManager(new LinearLayoutManager(this));

        cropsList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        addCrop = new Intent(getApplicationContext(), AddCropActivity.class);
        getLandDetails();
    }

    private void getCropDetails() {

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/getCropDetailsLand")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"landId\":\""+getIntent().getExtras().getString("landid")+"\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
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
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                cropId = obj.getString("cropid");
                                cropName = obj.getString("cropname");
                                cropPrice = obj.getString("price");
                                cropQuantity = obj.getString("quantity");

                                cropsList.add(new Crops(cropId, cropName, cropPrice, cropQuantity, "", ""));
                            }

                            adapter = new CropDetailsAdapterLand(DetailedLandBuyerActivity.this, cropsList);
                            mCropDetails.setAdapter(adapter);
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

    private void getLandDetails() {

        progressDialog.show();
        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/getLandDetailsFarmer")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"landId\":\""+getIntent().getExtras().getString("landid")+"\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = response.body().string();
                            JSONObject mainObj = new JSONObject(json);
                            if (!mainObj.getString("result").equals("failed")) {
                                mLandName.setText(mainObj.getString("landname"));
                                mLandArea.setText(mainObj.getString("landarea"));
                                mLandArea.append(" Meter Sq.");
                                mSoilType.setText(mainObj.getString("soiltype"));
                                mLandAddress.setText(mainObj.getString("landaddress"));
                                addCrop.putExtra("landname", mainObj.getString("landname"));

                                getCropDetails();
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
}

