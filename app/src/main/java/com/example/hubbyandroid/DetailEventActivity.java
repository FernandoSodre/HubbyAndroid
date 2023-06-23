package com.example.hubbyandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hubbyandroid.models.Evento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailEventActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView categoria;
    private TextView data;
    private TextView hora;
    private TextView local;
    private EditText descricao;
    private Button btnEntrar;
    private String eventoID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        eventoID = getIntent().getStringExtra("id");

        titulo = findViewById(R.id.textTitulo);
        categoria = findViewById(R.id.textCategoria);
        data = findViewById(R.id.textData);
        hora = findViewById(R.id.textHorario);
        local = findViewById(R.id.textLocal);
        descricao = findViewById(R.id.edtDescricao);
        btnEntrar = findViewById(R.id.buttonEntrar);

        descricao.setFocusable(false);


        buscaEvento(eventoID);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void buscaEvento(String eventoID){
        DatabaseReference eventosRef = FirebaseDatabase.getInstance().getReference("Eventos").child(eventoID);
        eventosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Evento evento = snapshot.getValue(Evento.class);
                    exibirDetalhesDoEvento(evento);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
    private void exibirDetalhesDoEvento(Evento evento){
        titulo.setText(evento.getTitulo());
        categoria.setText(evento.getCategoria());
        data.setText(evento.getData());
        hora.setText(evento.getHora());
        local.setText(evento.getLocal());
        descricao.setText(evento.getDescricao());
    }


}