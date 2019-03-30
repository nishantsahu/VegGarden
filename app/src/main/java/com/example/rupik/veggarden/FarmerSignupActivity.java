package com.example.rupik.veggarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FarmerSignupActivity extends AppCompatActivity {

    EditText mName, mEmail, mContact, mAadhar, mPass, mConfirmPass;
    Button mSignup;
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


    }
}
