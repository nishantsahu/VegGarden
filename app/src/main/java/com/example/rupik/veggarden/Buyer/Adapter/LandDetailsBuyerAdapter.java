package com.example.rupik.veggarden.Buyer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rupik.veggarden.Buyer.DetailedLandBuyerActivity;
import com.example.rupik.veggarden.Data.Lands;
import com.example.rupik.veggarden.R;

import java.util.List;

public class LandDetailsBuyerAdapter extends RecyclerView.Adapter<LandDetailsBuyerAdapter.LandDetailsViewHolder> {

    Context mCtx;
    List<Lands> landsList;

    public LandDetailsBuyerAdapter(Context mCtx, List<Lands> landsList) {
        this.mCtx = mCtx;
        this.landsList = landsList;
    }

    @Override
    public LandDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.land_details_card, parent, false);
        LandDetailsViewHolder landDetailsViewHolder = new LandDetailsViewHolder(view);
        return landDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(LandDetailsViewHolder holder, int position) {

        final Lands lands = landsList.get(position);

        holder.mLandName.setText(lands.getLandName());
        holder.mLandArea.setText(lands.getLandArea());
        holder.mLandLocation.setText(lands.getLandAddress());
        holder.mLandDetailCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(mCtx, DetailedLandBuyerActivity.class);
                detail.putExtra("landid", lands.getLandid());
                mCtx.startActivity(detail);
            }
        });

    }



    @Override
    public int getItemCount() {
        return landsList.size();
    }

    class LandDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView mLandName, mLandArea, mLandLocation;
        CardView mLandDetailCards;
        public LandDetailsViewHolder(View itemView) {
            super(itemView);

            mLandName = itemView.findViewById(R.id.farmerLandName);
            mLandArea = itemView.findViewById(R.id.farmerLandArea);
            mLandLocation = itemView.findViewById(R.id.farmerLandLocation);
            mLandDetailCards = itemView.findViewById(R.id.landDetailsCard);

        }
    }

}
