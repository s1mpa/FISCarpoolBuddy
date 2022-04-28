package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class VehiclesInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser currUser;
    private ArrayList<Vehicle> vehiclesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_info);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currUser = mAuth.getCurrentUser();

        vehiclesList = new ArrayList<>();
    }

    public void getAndPopulateData(View v)
    {
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
                System.out.println("--->" + vehiclesList.toString());
            }
        });

    }


}