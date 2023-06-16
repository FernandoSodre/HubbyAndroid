package com.example.hubbyandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hubbyandroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    private void criarEvento(){
        DatabaseReference countersRef = FirebaseDatabase.getInstance().getReference("Contador/contadorIDEvento");

        countersRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                Integer currentValue = currentData.getValue(Integer.class);
                if(currentValue == null){
                    currentData.setValue(0);
                } else{
                    currentData.setValue(currentValue + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot currentData) {
                if (committed){
                    Integer incrementedValue = currentData.getValue(Integer.class);
                }
            }
        });
    }
}