package com.example.rupik.veggarden;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class AddLandActivity extends AppCompatActivity {

    EditText mLandName, mLandArea, mAddress;
    Spinner mSoilType, mAreaUnit;
    Button mSubmit;
    float area = 0;
    String landName, landArea, areaUnit, soilType, address, lng, lat, uid;
    OkHttpClient client;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_land);

        mLandName = findViewById(R.id.landName);
        mLandArea = findViewById(R.id.landArea);
        mAreaUnit = findViewById(R.id.landAreaUnit);
        mSoilType = findViewById(R.id.landSoilType);
        mAddress = findViewById(R.id.landAddress);
        mSubmit = findViewById(R.id.submitAddLand);

        client = new OkHttpClient();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(AddLandActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();
                landName = mLandName.getText().toString();
                landArea = mLandArea.getText().toString();
                areaUnit = mAreaUnit.getSelectedItem().toString();
                soilType = mSoilType.getSelectedItem().toString();
                address = mAddress.getText().toString();
                lng = "20.1231231";
                lat = "21.214134";

                switch (areaUnit){
                    case "Meter Sq":
                        area = (float) (Float.parseFloat(landArea));
                        break;
                    case "Acre":
                        area = (float) (Float.parseFloat(landArea)*4046.8564);
                        break;
                    case "Hectare":
                        area = (float) (Float.parseFloat(landArea)*10000);
                        break;
                    case "Yard":
                        area = (float) (Float.parseFloat(landArea)*0.836);
                }

                if (TextUtils.isEmpty(landName))
                    mLandName.setError("Required");
                if (TextUtils.isEmpty(landArea))
                    mLandArea.setError("Required");
                if (TextUtils.isEmpty(address))
                    mAddress.setError("Required");
                if (TextUtils.isEmpty(landName) || TextUtils.isEmpty(landArea) || TextUtils.isEmpty(address))
                {
                    progressDialog.hide();
                }
                else {
                    addLand();
                }

            }
        });

    }

    private void addLand() {
        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/addLand")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"landName\":\""+landName+"\",\n" +
                        "\t\"landArea\":\""+area+"\",\n" +
                        "\t\"soilType\":\""+soilType+"\",\n" +
                        "\t\"address\":\""+address+"\",\n" +
                        "\t\"lng\":\""+lng+"\",\n" +
                        "\t\"lat\":\""+lat+"\",\n" +
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
                        Toast.makeText(AddLandActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddLandActivity.this, json, Toast.LENGTH_SHORT).show();
                            JSONObject mainObj = new JSONObject(json);
                            String result = mainObj.getString("result");

                            if (result.equals("success")) {
                                Intent landDetails = new Intent(getApplicationContext(), LandDetailsActivity.class);
                                startActivity(landDetails);
                            } else {
                                Toast.makeText(AddLandActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
