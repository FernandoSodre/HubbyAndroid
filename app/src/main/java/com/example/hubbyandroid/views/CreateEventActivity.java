package com.example.hubbyandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hubbyandroid.R;
import com.example.hubbyandroid.models.Evento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventLocale;
    private Spinner spinnerSelectCategory;
    private EditText editTextTitle;
    private EditText editTextDateEvent;
    private EditText editTextTimeEvent;
    private EditText editTextLocal;
    private EditText editTextDescription;
    private Button buttonCriarEvento;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private Double latitude;
    private Double longitude;
    private String locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        spinnerSelectCategory = findViewById(R.id.spinnerSelectCategory);
        editTextTitle = findViewById(R.id.editTitleEvent);
        editTextDateEvent = findViewById(R.id.editTextDate);
        editTextTimeEvent = findViewById(R.id.editTextTime);
        editTextLocal = findViewById(R.id.localeEvent);
        editTextDescription = findViewById(R.id.editTextTextMultiLine);
        buttonCriarEvento = findViewById((R.id.buttonEntrar));

        locale = "lat " + latitude + ", lon" + longitude;

        editTextLocal.setText( locale );

        buttonCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CriarEvento(editTextTitle.getText().toString(),editTextDateEvent.getText().toString(),editTextTimeEvent.getText().toString(),editTextLocal.getText().toString(),editTextDescription.getText().toString(),spinnerSelectCategory.getSelectedItem().toString());
                Intent intent = new Intent(CreateEventActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CriarEvento(String title, String date, String time, String local, String description, String category){
        DatabaseReference eventosRef = FirebaseDatabase.getInstance().getReference("Eventos");
        DatabaseReference novoEventoRef = eventosRef.push(); // Cria um novo nó com uma chave gerada automaticamente
        String novoEventoID = novoEventoRef.getKey(); // Obtém a chave gerada

        Evento novoEvento = new Evento(novoEventoID, title, date, time, local, description, category);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        novoEventoRef.setValue(novoEvento).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // O evento foi salvo com sucesso
                        Toast.makeText(getApplicationContext(), "Evento cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                        DatabaseReference participantesRef = FirebaseDatabase.getInstance().getReference("Eventos")
                                .child(novoEventoID)
                                .child("Participantes")
                                .child(userID);

                        participantesRef.setValue(true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // O participante foi salvo com sucesso
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        // Ocorreu um erro ao salvar o participante
                                        Toast.makeText(getApplicationContext(), "Erro ao adicionar participante: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Ocorreu um erro ao salvar o evento
                        Toast.makeText(getApplicationContext(), "Erro ao cadastrar evento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        eventLocale = findViewById(R.id.localeEvent);
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