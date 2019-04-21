package com.example.rupik.veggarden;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddCropActivity extends AppCompatActivity {

    TextView mLandName;
    EditText mCropName, mPrice, mQuantity;
    Button mSubmit;
    String landname, landid, uid;
    OkHttpClient client;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);

        mLandName = findViewById(R.id.landNameAddCrop);
        mCropName = findViewById(R.id.cropNameAddCrop);
        mPrice = findViewById(R.id.cropPriceAddCrop);
        mQuantity = findViewById(R.id.cropQuantityAddCrop);
        mSubmit = findViewById(R.id.submitAddCrop);
        landname = getIntent().getExtras().getString("landname");
        mLandName.setText(landname);
        landid = getIntent().getExtras().getString("landid");
        uid = FirebaseAuth.getInstance().getUid();
        progressDialog = new ProgressDialog(this);

        client = new OkHttpClient();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please wait..");
                progressDialog.show();
                String cropname, cropprice, cropquantity;

                cropname = mCropName.getText().toString();
                cropprice = mPrice.getText().toString();
                cropquantity = mPrice.getText().toString();

                Request request = new Request.Builder()
                        .url(Api.BASE_URL+"/addCrop")
                        .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                                "\t\"cropname\" : \""+cropname+"\",\n" +
                                "\t\"landid\" : \""+landid+"\",\n" +
                                "\t\"cropprice\" : \""+cropprice+"\",\n" +
                                "\t\"cropquantity\" : \""+cropquantity+"\",\n" +
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
                                Toast.makeText(AddCropActivity.this, "Check your connection.", Toast.LENGTH_SHORT).show();
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
                                    JSONObject mainObj = new JSONObject(json);
                                    String result = mainObj.getString("result");
                                    if (result.equals("success")) {
                                        Intent success = new Intent(getApplicationContext(), DetailLandActivity.class);
                                        success.putExtra("landid", landid);
                                        startActivity(success);
                                    } else {
                                        Toast.makeText(AddCropActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        });

    }
}
