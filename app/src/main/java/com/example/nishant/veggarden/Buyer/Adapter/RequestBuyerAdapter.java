package com.example.nishant.veggarden.Buyer.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nishant.veggarden.Data.Requests;
import com.example.nishant.veggarden.R;

import java.util.List;

public class RequestBuyerAdapter extends RecyclerView.Adapter<RequestBuyerAdapter.RequestDetailsViewHolder>{

    Context mCtx;
    List<Requests> requestsList;

    public RequestBuyerAdapter(Context mCtx, List<Requests> requestsList) {
        this.mCtx = mCtx;
        this.requestsList = requestsList;
    }

    @Override
    public RequestDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.request_buyer_card, parent, false);
        RequestDetailsViewHolder requestDetailsViewHolder = new RequestDetailsViewHolder(view);
        return requestDetailsViewHolder;
    }

    @Override
    public void onBindViewHolder(RequestDetailsViewHolder holder, int position) {

        Requests requests = requestsList.get(position);

        holder.mCropname.setText(requests.getCropname());
        holder.mFarmerName.setText(requests.getUname());
        holder.mDuration.setText(requests.getDuration() + " Months");
        if (requests.getAccepted().equals("true")) {
            holder.mAccepted.setText("Accepted");
            holder.mAccepted.setTextColor(mCtx.getResources().getColor(R.color.green));
        } else if (requests.getAccepted().equals("false")) {
            holder.mAccepted.setText("Rejected");
            holder.mAccepted.setTextColor(mCtx.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    class RequestDetailsViewHolder extends RecyclerView.ViewHolder {

        CardView mRequestBuyerCard;
        TextView mCropname, mFarmerName, mDuration, mAccepted;

        public RequestDetailsViewHolder(View itemView) {
            super(itemView);

            mRequestBuyerCard = itemView.findViewById(R.id.requestBuyerCard);
            mCropname = itemView.findViewById(R.id.requestCropName);
            mFarmerName = itemView.findViewById(R.id.requestFarmerName);
            mDuration = itemView.findViewById(R.id.requestDuration);
            mAccepted = itemView.findViewById(R.id.requestResponse);

        }
    }
}
