package com.example.rupik.veggarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DetailLandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_land);

        String uid = getIntent().getExtras().getString("uid");
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

    }
}
