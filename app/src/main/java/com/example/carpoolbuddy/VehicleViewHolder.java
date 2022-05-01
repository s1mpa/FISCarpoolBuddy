package com.example.carpoolbuddy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    protected TextView carOwner;
    protected TextView carModel;
    protected TextView carPrice;

    public VehicleViewHolder(@NonNull View itemView) {
        super(itemView);
        carOwner = itemView.findViewById(R.id.carOwnerText);
        carModel = itemView.findViewById(R.id.carModelText);
        carPrice = itemView.findViewById(R.id.carPriceText);
    }
}
