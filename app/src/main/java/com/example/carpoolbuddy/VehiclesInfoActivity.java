package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import io.opencensus.tags.Tag;

public class VehiclesInfoActivity extends AppCompatActivity implements VehicleViewHolder.OnNoteListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currUser;
    private ArrayList<Vehicle> vehiclesList;
    private EditText filterInput;
    private EditText filterInput2;


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

        vehiclesList.clear();

        TaskCompletionSource<String> getAllCarsTask = new TaskCompletionSource<>();
        firestore.collection("all-items").document("all-vehicles").collection("vehicles").whereEqualTo("open",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


                VehiclesAdapter myAdapter = new VehiclesAdapter(vehiclesList, VehiclesInfoActivity.this);
                recView.setAdapter(myAdapter);
                recView.setLayoutManager(new LinearLayoutManager(VehiclesInfoActivity.this));

            }
        });

    }

    public void back(View v)
    {
        Intent myIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(myIntent);
    }

    public void filter(View v) //price filter
    {
        vehiclesList.clear();
        filterInput = findViewById(R.id.filterPriceInput);
        filterInput2 = findViewById(R.id.filterPriceInput2);
        TaskCompletionSource<String> getAllCarsTask = new TaskCompletionSource<>();

        if(filterInput.getText().toString().isEmpty())
        {
            firestore.collection("all-items").document("all-vehicles").collection("vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() && task.getResult()!= null)
                    {
                        for(QueryDocumentSnapshot document : task.getResult())
                        {
                            if(document.toObject(Vehicle.class).isOpen())
                            {
                                vehiclesList.add(document.toObject(Vehicle.class));
                            }
                        }
                        getAllCarsTask.setResult(null);
                    }
                    else
                    {
                        Log.d("VehiclesInfoActivity","Error fetching vehicle info from firestore", task.getException());
                    }
                }
            });
        }
        else
        {
            double priceLimit = Double.parseDouble(filterInput.getText().toString());
            firestore.collection("all-items").document("all-vehicles").collection("vehicles").whereLessThanOrEqualTo("price", priceLimit).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() && task.getResult()!= null)
                    {
                        for(QueryDocumentSnapshot document : task.getResult())
                        {
                            if(document.toObject(Vehicle.class).isOpen())
                            {
                                vehiclesList.add(document.toObject(Vehicle.class));
                            }
                        }
                        getAllCarsTask.setResult(null);
                    }
                    else
                    {
                        Log.d("VehiclesInfoActivity","Error fetching vehicle info from firestore", task.getException());
                    }
                }
            });
        }



        //makes sure that all info has been retrieved before proceeding
        getAllCarsTask.getTask().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task)
            {
                if(filterInput2.getText().toString().isEmpty())
                {
                    VehiclesAdapter myAdapter = new VehiclesAdapter(vehiclesList, VehiclesInfoActivity.this);
                    recView.setAdapter(myAdapter);
                    recView.setLayoutManager(new LinearLayoutManager(VehiclesInfoActivity.this));
                }
                else
                {
                    double priceLimit2 = Double.parseDouble(filterInput2.getText().toString());
                    ArrayList<Vehicle> toRemove = new ArrayList<>();
                    for(Vehicle temp : vehiclesList)
                    {
                        if(temp.getPrice()<priceLimit2)
                        {
                            toRemove.add(temp);
                        }
                    }
                    vehiclesList.removeAll(toRemove);

                    VehiclesAdapter myAdapter = new VehiclesAdapter(vehiclesList, VehiclesInfoActivity.this);
                    recView.setAdapter(myAdapter);
                    recView.setLayoutManager(new LinearLayoutManager(VehiclesInfoActivity.this));

                }

            }
        });
    }


    @Override
    public void onNoteClick(int position) {
        System.out.println("--> clicked");
        Intent myIntent = new Intent(this, VehicleDetailsActivity.class);
        myIntent.putExtra("vehicles", (Parcelable) vehiclesList.get(position));
        startActivity(myIntent);
    }

}
