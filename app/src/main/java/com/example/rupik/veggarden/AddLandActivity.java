package com.example.rupik.veggarden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_land);

        mLandName = findViewById(R.id.landName);
        mLandArea = findViewById(R.id.landName);
        mAreaUnit = findViewById(R.id.landAreaUnit);
        mSoilType = findViewById(R.id.landSoilType);
        mAddress = findViewById(R.id.landAddress);
        mSubmit = findViewById(R.id.submitAddLand);

        client = new OkHttpClient();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String landName, landArea, areaUnit, soilType, address, lng, lat;
                landName = mLandName.getText().toString();
                landArea = mLandArea.getText().toString();
                areaUnit = mAreaUnit.getSelectedItem().toString();
                soilType = mSoilType.getSelectedItem().toString();
                address = mAddress.getText().toString();
                lng = "20.1231231";
                lat = "21.214134";

                Request request = new Request.Builder()
                        .url(Api.BASE_URL+"/addLand")
                        .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                                "\t\"landName\":\""+landName+"\",\n" +
                                "\t\"landArea\":\""+landArea+"\",\n" +
                                "\t\"soilType\":\""+soilType+"\",\n" +
                                "\t\"address\":\""+address+"\",\n" +
                                "\t\"lng\":\""+lng+"\",\n" +
                                "\t\"lat\":\""+lat+"\"\n" +
                                "}"))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                                    String json = response.body().string();
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
        });

    }
}
