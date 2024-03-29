package com.example.nishant.veggarden.Farmer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.nishant.veggarden.Farmer.Adapter.CropDetailsAdapter;
import com.example.nishant.veggarden.Api;
import com.example.nishant.veggarden.Data.Crops;
import com.example.nishant.veggarden.R;
import com.google.firebase.auth.FirebaseAuth;

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

public class CropDetailsFarmerActivity extends AppCompatActivity {

    RecyclerView mCropDetails;
    CropDetailsAdapter adapter;
    List<Crops> cropsList;
    OkHttpClient client;
    String cropId, cropName, landName, cropPrice, cropQuanity, cropLand;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_details_farmer);

        mCropDetails = findViewById(R.id.cropDetailsFarmer);
        cropsList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");

        mCropDetails.setLayoutManager(new LinearLayoutManager(this));
        client = new OkHttpClient();
        
        getCrops();
    }

    private void getCrops() {

        progressDialog.show();
        String uid = FirebaseAuth.getInstance().getUid();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/getCropDetailsUser")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"uid\" : \""+uid+"\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(CropDetailsFarmerActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressDialog.dismiss();
                            String json = response.body().string();
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                cropId = obj.getString("cropid");
                                landName = obj.getString("landname");
                                cropName = obj.getString("cropname");
                                cropPrice = obj.getString("price");
                                cropLand = obj.getString("landid");
                                cropQuanity = obj.getString("quantity");

                                cropsList.add(new Crops(cropId, cropName, cropPrice,cropQuanity, cropLand, landName));
                            }

                            adapter = new CropDetailsAdapter(getApplicationContext(), cropsList);
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
}
