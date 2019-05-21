package com.example.rupik.veggarden.Farmer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rupik.veggarden.Data.Crops;
import com.example.rupik.veggarden.Farmer.EditCropActivity;
import com.example.rupik.veggarden.R;

import java.util.List;

public class CropDetailsAdapterLand extends RecyclerView.Adapter<CropDetailsAdapterLand.CropDetailsLandViewHolder>{

    Context mCtx;
    List<Crops> cropsList;

    public CropDetailsAdapterLand(Context mCtx, List<Crops> cropsList) {
        this.mCtx = mCtx;
        this.cropsList = cropsList;
    }

    @Override
    public CropDetailsLandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.crop_details_card, parent, false);
        CropDetailsLandViewHolder cropDetailsLandViewHolder = new CropDetailsLandViewHolder(view);
        return cropDetailsLandViewHolder;
    }

    @Override
    public void onBindViewHolder(CropDetailsLandViewHolder holder, int position) {
        final Crops crops = cropsList.get(position);

        holder.mCropName.setText(crops.getCropName());
        holder.mCropPrice.setText(crops.getCropPrice());
        holder.mCropQuantity.setText(crops.getCropQuantity());
        holder.mCropDetailsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cropId = crops.getCropId();
                Intent editCrop = new Intent(mCtx, EditCropActivity.class);
                editCrop.putExtra("cropid", cropId);
                mCtx.startActivity(editCrop);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropsList.size();
    }

    class CropDetailsLandViewHolder extends RecyclerView.ViewHolder {

        TextView mCropName, mCropPrice, mCropQuantity;
        CardView mCropDetailsCard;
        public CropDetailsLandViewHolder(View itemView) {
            super(itemView);
            mCropName = itemView.findViewById(R.id.farmerCropName);
            mCropPrice = itemView.findViewById(R.id.farmerCropPrice);
            mCropQuantity = itemView.findViewById(R.id.farmerCropQuantity);
            mCropDetailsCard = itemView.findViewById(R.id.cropDetailsCard);
        }
    }
}
