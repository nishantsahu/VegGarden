package com.example.rupik.veggarden;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.rupik.veggarden.Adapters.LandDetailsAdapter;
import com.example.rupik.veggarden.Data.Lands;

import java.util.ArrayList;
import java.util.List;

public class LandDetailsActivity extends AppCompatActivity {

    RecyclerView mLandList;
    List<Lands> landsList;
    LandDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_details);

        mLandList = findViewById(R.id.landList);

        mLandList.setLayoutManager(new LinearLayoutManager(this));
        mLandList.setHasFixedSize(true);

        landsList = new ArrayList<>();

        landsList.add(new Lands("Land1", "300sqft", "Plot 234, NN Bhilai"));
        landsList.add(new Lands("Land1", "300sqft", "Plot 234, NN Bhilai"));
        landsList.add(new Lands("Land1", "300sqft", "Plot 234, NN Bhilai"));
        landsList.add(new Lands("Land1", "300sqft", "Plot 234, NN Bhilai"));
        landsList.add(new Lands("Land1", "300sqft", "Plot 234, NN Bhilai"));

        adapter = new LandDetailsAdapter(getApplication(), landsList);
        mLandList.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFarm = new Intent(getApplicationContext(), AddLandActivity.class);
                startActivity(addFarm);
            }
        });

    }
}
