package com.example.nishant.veggarden.Buyer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishant.veggarden.Api;
import com.example.nishant.veggarden.R;
import com.google.firebase.auth.FirebaseAuth;

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

public class RequestForCropActivity extends AppCompatActivity {

    TextView mFarmerID;
    EditText mCropName, mCropPrice, mCropQuantity;
    Spinner mDuration;
    Button mRequest;
    OkHttpClient client;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_crop);

        mCropName = findViewById(R.id.cropNameRequestCrop);
        mCropPrice = findViewById(R.id.cropPriceRequestCrop);
        mCropQuantity = findViewById(R.id.cropQuantityRequestCrop);
        mFarmerID = findViewById(R.id.farmerid);
        mDuration = findViewById(R.id.duration);
        mRequest = findViewById(R.id.submitRequestCrop);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        client = new OkHttpClient();

        getCropDetails();

        mRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {

        progressDialog.show();

        final String uid = FirebaseAuth.getInstance().getUid();
        String farmerid = mFarmerID.getText().toString();
        String cropid = getIntent().getExtras().getString("cropid");
        String duration = mDuration.getSelectedItem().toString();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/sendAgreementRequest")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"cropname\" : \""+mCropName.getText().toString()+"\",\n" +
                        "\t\"cropid\" : \""+cropid+"\",\n" +
                        "\t\"buyerid\" : \""+uid+"\",\n" +
                        "\t\"farmerid\" : \""+farmerid+"\",\n" +
                        "\t\"duration\" : \""+duration+"\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RequestForCropActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                JSONObject mainObj = new JSONObject(result);
                                String res = mainObj.getString("result");
                                if (res.equals("success")) {
                                    Toast.makeText(RequestForCropActivity.this, "Request sent", Toast.LENGTH_SHORT).show();
                                    Intent request = new Intent(getApplicationContext(), BuyerRequestActivity.class);
                                    startActivity(request);
                                } else {
                                    Toast.makeText(RequestForCropActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
            }
        });

    }

    private void getCropDetails() {

        progressDialog.show();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/getCropDetailForEdit")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"cropid\" : \""+getIntent().getExtras().getString("cropid")+"\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(RequestForCropActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                String result = response.body().string();
                                JSONObject mainObj = new JSONObject(result);
                                if (mainObj.getString("result").equals("success")) {
                                    mFarmerID.setText(mainObj.getString("farmerid"));
                                    mCropName.setText(mainObj.getString("cropname"));
                                    mCropPrice.setText(mainObj.getString("cropprice"));
                                    mCropQuantity.setText(mainObj.getString("cropquantity"));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }
}
