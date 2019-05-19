package com.example.rupik.veggarden;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

public class BuyerDashboardActivity extends AppCompatActivity {
    TextView mName;
    OkHttpClient client;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);
        mName = findViewById(R.id.buyername);

        progressDialog = new ProgressDialog(this);

        checkUserExistance();

    }

    private void checkUserExistance() {

        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        client = new OkHttpClient();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        Request request = new Request.Builder()
                .url(Api.BASE_URL+"/checkUserExistanceBuyer")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
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
                        Toast.makeText(BuyerDashboardActivity.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                        Intent auth = new Intent(getApplicationContext(), AuthFarmerActivity.class);
                        startActivity(auth);
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
                            JSONObject mainObj = new JSONObject(json);
                            String result = mainObj.getString("result");
                            if (result.equals("success")) {
                                String name = mainObj.getString("name");
                                mName.setText(name);
                            } else if (result.equals("none")) {
                                Intent signup = new Intent(getApplicationContext(), BuyerSignupActivity.class);
                                startActivity(signup);
                            } else {
                                Intent auth = new Intent(getApplicationContext(), AuthBuyerActivity.class);
                                startActivity(auth);
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

    @Override
    public void onBackPressed() {
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
    }
}
