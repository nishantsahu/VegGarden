package com.example.rupik.veggarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView mName, mContact;
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);
        mName = findViewById(R.id.buyername);
        mContact = findViewById(R.id.buyercontact);

        getuser();
    }

    private void getuser() {
        String uid,name,contact;
        uid="34";
        client=new OkHttpClient();
        Request request=new Request.Builder().url("https://veggarden123.herokuapp.com/getUser").post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                "            \"uid\" : \""+uid+"\"\n" +
                "        }")).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BuyerDashboardActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json=response.body().string();
                            JSONObject jsonobj=new JSONObject(json);
                            String name=jsonobj.getString("name");
                            String contact=jsonobj.getString("contact");
                            mName.setText(name);
                            mContact.setText(contact);
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
