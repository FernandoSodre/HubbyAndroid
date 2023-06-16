package com.example.hubbyandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.hubbyandroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.core.view.View;

import java.util.ArrayList;

public class CreateEventActivity extends AppCompatActivity {

    private Spinner spinnerSelectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Log.d("estate", "indo criar");

        spinnerSelectCategory = findViewById(R.id.spinnerSelectCategory);
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

    private void populateSpinner()
    {
        spinnerSelectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                // Lógica para tratar a opção selecionada
                String selectedOption = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Lógica quando nenhuma opção é selecionada (opcional)
            }
        });
    }
}