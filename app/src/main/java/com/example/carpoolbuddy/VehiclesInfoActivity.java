package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class VehiclesInfoActivity extends AppCompatActivity implements VehiclesAdapter.OnNoteListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currUser;
    private ArrayList<Vehicle> vehiclesList;
    private ArrayList<String> carOwnersList;
    private ArrayList<String> carModelsList;
    private ArrayList<String> carPricesList;


    RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_info);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currUser = mAuth.getCurrentUser();
        vehiclesList = new ArrayList<>();
        recView = findViewById(R.id.vehiclesRecView);

        String userID = currUser.getUid();
        vehiclesList.clear();
        TaskCompletionSource<String> getAllCarsTask = new TaskCompletionSource<>();
        firestore.collection("all-users").document(userID).collection("vehicles").whereEqualTo("open",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult()!= null)
                {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        vehiclesList.add(document.toObject(Vehicle.class));
                    }
                    getAllCarsTask.setResult(null);
                }
                else
                {
                    Log.d("VehiclesInfoActivity","Error fetching vehicle info from firestore", task.getException());
                }
            }
        });

        //makes sure that all info has been retrieved before proceeding
        getAllCarsTask.getTask().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                for(Vehicle v : vehiclesList)
                {
                    System.out.println("Retrieved vehicle --->" + v.getVehicleID()); //just to check it was able to get all vehicles
                }

                carOwnersList = new ArrayList<>();
                carModelsList = new ArrayList<>();
                carPricesList = new ArrayList<>();

                for(Vehicle v : vehiclesList)
                {
                    carOwnersList.add(v.getOwner());
                    carModelsList.add(v.getModel());
                    carPricesList.add(Double.toString(v.getPrice()));
                }

                //to check all info is retrieved correctly for rec view
                System.out.println("Owners: --->" + carOwnersList.toString());
                System.out.println("Models: --->" + carModelsList.toString());
                System.out.println("Prices: --->" + carPricesList.toString());

                VehiclesAdapter myAdapter = new VehiclesAdapter(carOwnersList,carModelsList,carPricesList);
                recView.setAdapter(myAdapter);
                recView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });

    }


    @Override
    public void onNoteClick(int position) {
        Intent myIntent = new Intent(this, VehicleDetailsActivity.class);
        startActivity(myIntent);
    }
}