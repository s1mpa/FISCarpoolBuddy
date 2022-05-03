package com.example.carpoolbuddy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class VehicleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected TextView carOwner;
    protected TextView carModel;
    protected TextView carPrice;
    private OnNoteListener myOnNoteListener;


    public VehicleViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
        super(itemView);
        carOwner = itemView.findViewById(R.id.carOwnerText);
        carModel = itemView.findViewById(R.id.carModelText);
        carPrice = itemView.findViewById(R.id.carPriceText);

        this.myOnNoteListener = onNoteListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {

        myOnNoteListener.onNoteClick(getAdapterPosition());
    }

    public interface OnNoteListener
    {
        void onNoteClick(int position);
    }

}
