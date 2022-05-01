package com.example.carpoolbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VehiclesAdapter extends RecyclerView.Adapter<VehicleViewHolder>
{
    ArrayList<String> myData;
    ArrayList<String> myData2;
    ArrayList<String> myData3;

    public VehiclesAdapter(ArrayList<String> myData, ArrayList<String> myData2, ArrayList<String> myData3)
    {
        this.myData = myData;
        this.myData2 = myData2;
        this.myData3 = myData3;
    }


    @NonNull
    @Override

    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_row_layout,parent,false);
        VehicleViewHolder myHolder = new VehicleViewHolder(myView);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        holder.carOwner.setText(myData.get(position));
        holder.carModel.setText(myData2.get(position));
        holder.carPrice.setText(myData3.get(position));
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }


    //WIP clickable rec view
    public interface OnNoteListener
    {
        void onNoteClick(int position);
    }
}