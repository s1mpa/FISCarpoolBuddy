package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddVehicleActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
    }

    public void createNewVehicle(View v)
    {
        EditText model = findViewById(R.id.carModelInput);
        EditText capacity = findViewById(R.id.capacityInput);
        EditText type = findViewById(R.id.carTypeInput);
        EditText price = findViewById(R.id.priceInput);
        Switch open = findViewById(R.id.openBoolean);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Vehicle newCar = new Vehicle(currentUser.getEmail(), ... work on next time);
    }
}