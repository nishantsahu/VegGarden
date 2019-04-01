package com.example.rupik.veggarden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
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

public class BuyerSignupActivity extends AppCompatActivity {

    EditText mName, mEmail, mContact, mAadhar, mPass, mConfirmPass;
    String name, email,contact,aadhar,password,confirmpass;
    OkHttpClient client;
    Button mSignup;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_signup);

        mName = findViewById(R.id.bsignname);
        mEmail = findViewById(R.id.bsignemail);
        mContact = findViewById(R.id.bsigncontact);
        mAadhar = findViewById(R.id.bsignadhar);
        mPass = findViewById(R.id.bsignpass);
        mConfirmPass = findViewById(R.id.bsigncpass);
        mSignup = findViewById(R.id.bsignupbtn);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = mName.getText().toString();
                email = mEmail.getText().toString();
                contact = mContact.getText().toString();
                aadhar = mAadhar.getText().toString();
                password = mPass.getText().toString();
                confirmpass = mConfirmPass.getText().toString();

                client = new OkHttpClient();

                if (TextUtils.isEmpty(name))
                    mName.setError("Required");
                if (TextUtils.isEmpty(email))
                    mEmail.setError("Required");
                if (TextUtils.isEmpty(contact))
                    mContact.setError("Required");
                if (TextUtils.isEmpty(aadhar))
                    mAadhar.setError("Required");
                if (TextUtils.isEmpty(password))
                    mPass.setError("Required");
                if (TextUtils.isEmpty(confirmpass))
                    mConfirmPass.setError("Required");



                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(aadhar)||TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmpass))
                {}
                else {
                    if(confirmpass.equals(password))
                    {
                        addUser();
                    }
                    else {
                        mConfirmPass.setError("Password is not same");
                    }
                }




            }
        });

    }

    private void addUser() {

        Request request=new Request.Builder()
                .url("https://veggarden123.herokuapp.com/addUser")
                .post(RequestBody.create(MediaType.parse("application/json"), "{\n" +
                        "\t\"names\" : \""+name+"\",\n" +
                        "\t\"email\" : \""+email+"\",\n" +
                        "\t\"contact\" : \""+contact+"\",\n" +
                        "\t\"aadhar\" : \""+aadhar+"\",\n" +
                        "\t\"password\" : \""+password+"\"\n" +
                        "}")).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(BuyerSignupActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json=response.body().string();
                try {
                    JSONObject mainobj=new JSONObject(json);
                    String result=mainobj.getString("result");
                    if(result.equals("success"))
                    {
                        Intent i=new Intent(getApplicationContext(),BuyerDashboardActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(BuyerSignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
