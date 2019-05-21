package com.example.rupik.veggarden.Farmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rupik.veggarden.Api;
import com.example.rupik.veggarden.R;

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

public class EditCropActivity extends AppCompatActivity {

    EditText mCropNameEditCrop, mCropPriceEditCrop, mCropQuantityEditCrop;
    Button mSubmitEditCrop;
    String cropName, cropPrice, cropQuantity;
    OkHttpClient client;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_crop);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        mCropNameEditCrop = findViewById(R.id.cropNameEditCrop);
        mCropPriceEditCrop = findViewById(R.id.cropPriceEditCrop);
        mCropQuantityEditCrop = findViewById(R.id.cropQuantityEditCrop);
        mSubmitEditCrop = findViewById(R.id.submitEditCrop);

        client = new OkHttpClient();
        fillDataFromDatabase();

        mSubmitEditCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });

    }

    public void fillDataFromDatabase() {
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
                        Toast.makeText(EditCropActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
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
                                    mCropNameEditCrop.setText(mainObj.getString("cropname"));
                                    mCropPriceEditCrop.setText(mainObj.getString("cropprice"));
                                    mCropQuantityEditCrop.setText(mainObj.getString("cropquantity"));
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

    public void validateForm(){
        cropName = mCropNameEditCrop.getText().toString();
        cropPrice = mCropPriceEditCrop.getText().toString();
        cropQuantity = mCropQuantityEditCrop.getText().toString();

        if (TextUtils.isEmpty(cropName)) {
            mCropNameEditCrop.setError("Required");
        }
        if (TextUtils.isEmpty(cropPrice)) {
            mCropPriceEditCrop.setError("Required");
        }
        if (TextUtils.isEmpty(cropQuantity)) {
            mCropQuantityEditCrop.setError("Required");
        }

        if (TextUtils.isEmpty(cropName) || TextUtils.isEmpty(cropPrice) || TextUtils.isEmpty(cropQuantity)) {

        } else {
            updateCropDetails();
        }

    }

    public void updateCropDetails() {

        progressDialog.show();

        cropName = mCropNameEditCrop.getText().toString();
        cropPrice = mCropPriceEditCrop.getText().toString();
        cropQuantity = mCropQuantityEditCrop.getText().toString();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/updateRequestEditCrop")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"cropid\" : \""+getIntent().getExtras().getString("cropid")+"\",\n" +
                        "\t\"cropname\" : \""+cropName+"\",\n" +
                        "\t\"cropprice\" : \""+cropPrice+"\",\n" +
                        "\t\"cropquantity\" : \""+cropQuantity+"\"\n" +
                        "}"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(EditCropActivity.this, "Please check your connection.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(EditCropActivity.this, "Crop Details is updated", Toast.LENGTH_SHORT).show();
                                    Intent done = new Intent(getApplicationContext(), DetailLandActivity.class);
                                    done.putExtra("landid", mainObj.getString("landid"));
                                    startActivity(done);
                                } else {
                                    Toast.makeText(EditCropActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
