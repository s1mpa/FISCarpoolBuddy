package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class VehicleDetailsActivity extends AppCompatActivity {

    private Vehicle selectedVehicle;
    private TextView owner;
    private TextView model;
    private TextView price;
    private TextView maxCap;
    private TextView currCap;
    private TextView bookedList;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        owner = findViewById(R.id.vehicleOwnerText);
        model = findViewById(R.id.vehicleModelText);
        price = findViewById(R.id.vehiclePriceText);
        maxCap = findViewById(R.id.maxCapacityText);
        currCap = findViewById(R.id.currCapacityText);
        bookedList = findViewById(R.id.bookedUsersText);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        if(getIntent().hasExtra("vehicles"))
        {
            selectedVehicle = getIntent().getParcelableExtra("vehicles");
            owner.setText(selectedVehicle.getOwner());
            model.setText(selectedVehicle.getModel());
            price.setText(String.valueOf(selectedVehicle.getPrice()));
            maxCap.setText(Integer.toString(selectedVehicle.getCapacity()));
            currCap.setText(Integer.toString(selectedVehicle.getCurrCapacity()));
            bookedList.setText(selectedVehicle.getRidersUIDs().toString());

        }
    }

    public void bookRide(View v)
    {
        selectedVehicle.addRiderUID(mUser.getUid());
        firestore.collection("all-users").document(mUser.getUid()).collection("vehicles").document(selectedVehicle.getVehicleID()).update("riderUIDs", selectedVehicle.getRidersUIDs()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                int temp = selectedVehicle.getCapacity() - 1;
                selectedVehicle.setCurrCapacity(temp);
                firestore.collection("all-users").document(mUser.getUid()).collection("vehicles").document(selectedVehicle.getVehicleID()).update("currCapacity", temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), VehiclesInfoActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }


}