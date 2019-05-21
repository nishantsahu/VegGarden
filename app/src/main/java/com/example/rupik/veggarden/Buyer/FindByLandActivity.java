package com.example.rupik.veggarden.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.rupik.veggarden.Buyer.Adapter.LandDetailsBuyerAdapter;
import com.example.rupik.veggarden.Api;
import com.example.rupik.veggarden.Data.Lands;
import com.example.rupik.veggarden.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FindByLandActivity extends AppCompatActivity {

    RecyclerView mLandList;
    List<Lands> landsList;
    LandDetailsBuyerAdapter adapter;
    OkHttpClient client;
    String uid;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_land);

        mLandList = findViewById(R.id.landListForBuyers);

        mLandList.setLayoutManager(new LinearLayoutManager(this));
        mLandList.setHasFixedSize(true);

        landsList = new ArrayList<>();

        client = new OkHttpClient();

        uid = FirebaseAuth.getInstance().getUid();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/getLandDetailsBuyer")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your connection.", Toast.LENGTH_SHORT).show();
                        Intent auth = new Intent(getApplicationContext(), AuthBuyerActivity.class);
                        startActivity(auth);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressDialog.hide();
                            String json = response.body().string();
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                landsList.add(new Lands(obj.getString("landname"), ""+obj.getString("landarea")+" meter sq.", obj.getString("address"), obj.getString("landid")));
                            }
                            adapter = new LandDetailsBuyerAdapter(getApplication(), landsList);
                            mLandList.setAdapter(adapter);
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
