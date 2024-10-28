package com.example.firebase;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private View light;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        light = findViewById(R.id.light);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                checkFirebaseStatus();
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);
    }

    private void checkFirebaseStatus()
    {
        mDatabase.child("status").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    int status = snapshot.getValue(Integer.class);
                    if (status == 1) {
                        light.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else {
                        light.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                }
            } else {
                handleFirebaseError(task.getException());
            }
        });
    }

    private void handleFirebaseError(Exception error)
    {
        if (error != null) {
            Log.e("Firebase Error", error.getMessage());
            // Notify the user
            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

        }
    }
}

