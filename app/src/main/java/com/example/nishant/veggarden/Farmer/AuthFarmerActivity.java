package com.example.nishant.veggarden.Farmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nishant.veggarden.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthFarmerActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText mFarmerContactNumber;
    Button mSendVerificationCode;
    ProgressDialog progressDialog;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_farmer);

        mFarmerContactNumber = findViewById(R.id.farmerContactNumber);
        mSendVerificationCode = findViewById(R.id.sendVerificationCode);
        mAuth = FirebaseAuth.getInstance();
        checkUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("User Authentication");
        progressDialog.setMessage("Scanning otp from your message..");
        mSendVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                loginUser();
            }
        });
    }

    private void loginUser() {

        String phone = "+91"+mFarmerContactNumber.getText().toString();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                mAuth.signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isComplete()) {
                                    progressDialog.dismiss();
                                    FirebaseUser user = task.getResult().getUser();
                                    Intent farmerDashboard = new Intent(getApplicationContext(), FarmerDashboardActivity.class);
                                    startActivity(farmerDashboard);
                                }
                            }
                        });

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(AuthFarmerActivity.this, "Something went wrong, Make sure you have inserted the same sim card.", Toast.LENGTH_SHORT).show();
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                AuthFarmerActivity.this,
                mCallbacks
        );

    }

    private void checkUser() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent farmerDashboard = new Intent(getApplicationContext(), FarmerDashboardActivity.class);
            startActivity(farmerDashboard);
        }

    }
}
