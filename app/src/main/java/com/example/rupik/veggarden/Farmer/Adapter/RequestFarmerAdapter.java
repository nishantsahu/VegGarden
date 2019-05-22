package com.example.rupik.veggarden.Farmer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rupik.veggarden.Data.Requests;
import com.example.rupik.veggarden.Farmer.RespondFarmerActivity;
import com.example.rupik.veggarden.R;

import java.util.List;

public class RequestFarmerAdapter extends RecyclerView.Adapter<RequestFarmerAdapter.RequestDetailsViewHolder>{

    Context mCtx;
    List<Requests> requestsList;

    public RequestFarmerAdapter(Context mCtx, List<Requests> requestsList) {
        this.mCtx = mCtx;
        this.requestsList = requestsList;
    }

    @Override
    public RequestDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.request_farmer_card, parent, false);
        RequestDetailsViewHolder requestDetailsViewHolder = new RequestDetailsViewHolder(view);
        return requestDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(RequestDetailsViewHolder holder, int position) {

        final Requests requests = requestsList.get(position);

        holder.mCropname.setText(requests.getCropname());
        holder.mBuyerName.setText(requests.getUname());
        holder.mDuration.setText(requests.getDuration());
        holder.mDate.setText(requests.getCreated_at());
        holder.mRequestFarmerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent respond = new Intent(mCtx, RespondFarmerActivity.class);
                respond.putExtra("agreementid", requests.getAgreementid());
                mCtx.startActivity(respond);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    class RequestDetailsViewHolder extends RecyclerView.ViewHolder {

        CardView mRequestFarmerCard;
        TextView mCropname, mBuyerName, mDuration, mDate;

        public RequestDetailsViewHolder(View itemView) {
            super(itemView);

            mRequestFarmerCard = itemView.findViewById(R.id.requestFarmerCard);
            mCropname = itemView.findViewById(R.id.requestCropNameFarmer);
            mBuyerName = itemView.findViewById(R.id.buyernameFarmer);
            mDuration = itemView.findViewById(R.id.requestDurationFarmer);
            mDate = itemView.findViewById(R.id.requestTimeFarmer);

        }
    }
}
