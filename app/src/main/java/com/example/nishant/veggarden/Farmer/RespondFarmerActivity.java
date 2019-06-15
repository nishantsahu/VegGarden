package com.example.nishant.veggarden.Farmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishant.veggarden.Api;
import com.example.nishant.veggarden.R;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
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

public class RespondFarmerActivity extends AppCompatActivity {

    TextView mBuyerName, mCropName, mBuyerContact, mBuyerAddress, mBuyerEmail, mDuration;
    Button mAccept, mDecline;
    OkHttpClient client;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond_farmer);

        mBuyerName = findViewById(R.id.farmerRespondBuyerName);
        mCropName = findViewById(R.id.farmerRespondCropName);
        mBuyerContact = findViewById(R.id.farmerRespondBuyerContact);
        mBuyerAddress = findViewById(R.id.farmerRespondBuyerAddress);
        mBuyerEmail = findViewById(R.id.farmerRespondBuyerEmail);
        mDuration = findViewById(R.id.farmerRespondDuration);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        client = new OkHttpClient();

        mAccept = findViewById(R.id.farmerRespondAccept);
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptFunction();
            }
        });
        mDecline = findViewById(R.id.farmerRespondDecline);
        mDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineFunction();
            }
        });

        fillDataOnline();

    }

    private void declineFunction() {
        progressDialog.show();
        final Request request = new Request.Builder()
                .url(Api.BASE_URL+"/respondAgreementRequest")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"requestid\" : \""+getIntent().getExtras().getString("agreementid")+"\",\n" +
                        "\t\"response\" : \"false\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RespondFarmerActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
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
                            String result = response.body().string();
                            JSONObject jsonObject = new JSONObject(result);
                            String res = jsonObject.getString("result");
                            if (res.equals("success")) {
                                Intent success = new Intent(getApplicationContext(), FarmerRequestActivity.class);
                                startActivity(success);
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

    private void acceptFunction() {

        progressDialog.show();
        final Request request = new Request.Builder()
                .url(Api.BASE_URL+"/respondAgreementRequest")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"requestid\" : \""+getIntent().getExtras().getString("agreementid")+"\",\n" +
                        "\t\"response\" : \"true\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RespondFarmerActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
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
                            JSONObject jsonObject = new JSONObject(result);
                            String res = jsonObject.getString("result");
                            if (res.equals("success")) {
                                Intent success = new Intent(getApplicationContext(), FarmerRequestActivity.class);
                                startActivity(success);
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

    private void fillDataOnline() {
        progressDialog.show();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/getAgreementsList")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"uid\" : \""+ FirebaseAuth.getInstance().getUid()+"\",\n" +
                        "\t\"type\" : \"farmer\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RespondFarmerActivity.this, "Please check your connection.", Toast.LENGTH_SHORT).show();
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
                            String agreementidoffline;
                            agreementidoffline = getIntent().getExtras().getString("agreementid");
                            JSONArray jsonArray = new JSONArray(result);
                            String agreementidarray[] = new String[jsonArray.length()];
                            for (int i=0; i<jsonArray.length(); i++) {
                                agreementidarray[i] = jsonArray.getJSONObject(i).getString("agreementid");
                            }
                            for(int i=0; i<jsonArray.length(); i++) {
                                if (agreementidarray[i].equals(agreementidoffline)) {
                                    mBuyerName.setText(jsonArray.getJSONObject(i).getString("name"));
                                    mBuyerContact.setText(jsonArray.getJSONObject(i).getString("contact"));
                                    mBuyerEmail.setText(jsonArray.getJSONObject(i).getString("email"));
                                    mBuyerAddress.setText(jsonArray.getJSONObject(i).getString("address"));
                                    mCropName.setText(jsonArray.getJSONObject(i).getString("cropname"));
                                    mDuration.setText(jsonArray.getJSONObject(i).getString("duration"));
                                }
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
