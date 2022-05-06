package com.example.carpoolbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VehiclesAdapter extends RecyclerView.Adapter<VehicleViewHolder>
{
    private ArrayList<Vehicle> myData;
    private VehicleViewHolder.OnNoteListener mOnNoteListener;

    public VehiclesAdapter(ArrayList<Vehicle> myData, VehicleViewHolder.OnNoteListener onNoteListener)
    {
        this.myData = myData;
        this.mOnNoteListener = onNoteListener;
    }


    @NonNull
    @Override

    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_row_layout,parent,false);
        return new VehicleViewHolder(myView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        holder.carOwner.setText(myData.get(position).getOwner());
        holder.carModel.setText(myData.get(position).getModel());
        holder.carPrice.setText(Double.toString(myData.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }


    //WIP clickable rec view

}
