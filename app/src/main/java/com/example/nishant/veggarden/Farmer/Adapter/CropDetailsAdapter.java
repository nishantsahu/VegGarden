package com.example.nishant.veggarden.Farmer.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nishant.veggarden.Data.Crops;
import com.example.nishant.veggarden.R;

import java.util.List;

public class CropDetailsAdapter extends RecyclerView.Adapter<CropDetailsAdapter.CropDetailViewHolder>{
    Context mCtx;
    List<Crops> cropsList;

    public CropDetailsAdapter(Context mCtx, List<Crops> cropsList) {
        this.mCtx = mCtx;
        this.cropsList = cropsList;
    }

    @Override
    public CropDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.crop_details_card_farmer, parent, false);
        CropDetailViewHolder cropDetailViewHolder = new CropDetailViewHolder(view);
        return cropDetailViewHolder;
    }

    @Override
    public void onBindViewHolder(CropDetailViewHolder holder, int position) {
        final Crops crops = cropsList.get(position);

        holder.landName.setText(crops.getLandName());
        holder.cropName.setText(crops.getCropName());
        holder.cropPrice.setText(crops.getCropPrice());
        holder.cropQuantity.setText(crops.getCropQuantity());
        holder.cropDetailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCtx, crops.getCropId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropsList.size();
    }

    class CropDetailViewHolder extends RecyclerView.ViewHolder {

        TextView landName, cropName, cropPrice, cropQuantity;
        CardView cropDetailCard;
        public CropDetailViewHolder(View itemView) {
            super(itemView);
            landName = itemView.findViewById(R.id.farmerLandNameFarmer);
            cropName = itemView.findViewById(R.id.farmerCropNameFarmer);
            cropPrice = itemView.findViewById(R.id.farmerCropPriceFarmer);
            cropQuantity = itemView.findViewById(R.id.farmerCropQuantityFarmer);
            cropDetailCard = itemView.findViewById(R.id.cropDetailsCardFarmer);
        }
    }
}
