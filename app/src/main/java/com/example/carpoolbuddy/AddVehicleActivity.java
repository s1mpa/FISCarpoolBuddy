package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

public class AddVehicleActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currUser= mAuth.getCurrentUser();
    }

    public void createNewVehicle(View v)
    {
        DocumentReference newCarRef = firestore.collection("vehicles").document();
        String vehicleID = newCarRef.getId();

        EditText name = findViewById(R.id.nameInput);
        EditText model = findViewById(R.id.carModelInput);
        EditText capacityString = findViewById(R.id.capacityInput);
        EditText type = findViewById(R.id.carTypeInput);
        EditText priceString = findViewById(R.id.priceInput);

        String userID = currUser.getUid();

        int capacity = Integer.parseInt(capacityString.getText().toString());
        double price = Double.parseDouble(priceString.getText().toString());

        Vehicle newCar = new Vehicle(name.getText().toString(),model.getText().toString(),capacity, vehicleID,new ArrayList<>(),true, type.getText().toString(), price);
        firestore.collection("all-users").document(userID).collection("vehicles").document(newCar.getVehicleID()).set(newCar);

    }
}