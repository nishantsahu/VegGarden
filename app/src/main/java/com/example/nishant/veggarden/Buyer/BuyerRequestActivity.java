package com.example.nishant.veggarden.Buyer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.nishant.veggarden.Api;
import com.example.nishant.veggarden.Buyer.Adapter.RequestBuyerAdapter;
import com.example.nishant.veggarden.Data.Requests;
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

public class BuyerRequestActivity extends AppCompatActivity {

    RecyclerView requestListBuyer;
    RequestBuyerAdapter adapter;
    List<Requests> requestsList;
    OkHttpClient client;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_request);

        requestListBuyer = findViewById(R.id.requestListBuyer);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        requestListBuyer.setLayoutManager(new LinearLayoutManager(this));
        requestListBuyer.setHasFixedSize(true);

        requestsList = new ArrayList<>();

        client = new OkHttpClient();
        getAllRequests();
    }

    private void getAllRequests() {

        progressDialog.show();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/getAgreementsList")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"uid\" : \""+FirebaseAuth.getInstance().getUid()+"\",\n" +
                        "\t\"type\" : \"buyer\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(BuyerRequestActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        try {
                            String result = response.body().string();
                            JSONArray jsonArray = new JSONArray(result);
                            for (int i=jsonArray.length()-1; i>=0; i--) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                requestsList.add(new Requests(jsonObject.getString("agreementid"),
                                        jsonObject.getString("buyerid"),
                                        jsonObject.getString("farmerid"),
                                        jsonObject.getString("cropid"),
                                        jsonObject.getString("duration"),
                                        jsonObject.getString("created_at"),
                                        jsonObject.getString("accepted"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("contact"),
                                        jsonObject.getString("address"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("cropname")));
                            }
                            adapter = new RequestBuyerAdapter(BuyerRequestActivity.this, requestsList);
                            requestListBuyer.setAdapter(adapter);
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
