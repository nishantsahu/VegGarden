package com.example.rupik.veggarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class FarmerSignupActivity extends AppCompatActivity {

    EditText mName, mEmail, mContact, mAadhar, mPass, mConfirmPass;
    Button mSignup;
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_signup);
        mName = findViewById(R.id.fsignname);
        mEmail = findViewById(R.id.fsignemail);
        mContact = findViewById(R.id.fsigncontact);
        mAadhar = findViewById(R.id.fsignadhar);
        mPass = findViewById(R.id.fsignpass);
        mConfirmPass = findViewById(R.id.fsigncpass);
        mSignup = findViewById(R.id.fsignupbtn);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, email, contact, aadhar, pass, cpass;
                name = mName.getText().toString();
                email = mEmail.getText().toString();
                contact = mContact.getText().toString();
                aadhar = mAadhar.getText().toString();
                pass = mPass.getText().toString();
                cpass = mConfirmPass.getText().toString();

                client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://veggarden123.herokuapp.com/addUser")
                        .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                                "\t\"name\" : \""+name+"\",\n" +
                                "\t\"email\" : \""+email+"\",\n" +
                                "\t\"contact\" : \""+contact+"\",\n" +
                                "\t\"aadhar\" : \""+aadhar+"\",\n" +
                                "\t\"password\" : \""+pass+"\"\n" +
                                "}")).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FarmerSignupActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
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
                                    JSONObject res = mainObj.getJSONObject("result");
                                    String result = res.getString("result");
                                    Toast.makeText(FarmerSignupActivity.this, result, Toast.LENGTH_SHORT).show();
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
