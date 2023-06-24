package com.example.hubbyandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hubbyandroid.adapter.ParticipanteAdapter;
import com.example.hubbyandroid.models.Evento;
import com.example.hubbyandroid.models.Participante;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailEventActivity extends AppCompatActivity {

    private TextView titulo;
    private TextView categoria;
    private TextView data;
    private TextView hora;
    private TextView local;
    private EditText descricao;
    private Button btnEntrar;
    private String eventoID;

    private ParticipanteAdapter participanteAdapter;
    private RecyclerView recyclerView;
    private List<Participante> participantes;

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

        recyclerView = findViewById(R.id.recyclerViewParticipantes);
        participantes = new ArrayList<>();
        participanteAdapter = new ParticipanteAdapter(participantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(participanteAdapter);

        buscaEvento(eventoID);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String participanteID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                adicionarParticipante(eventoID,participanteID);
                carregarParticipantes(eventoID);
            }
        });
    }

    private void buscaEvento(String eventoID) {
        DatabaseReference eventosRef = FirebaseDatabase.getInstance().getReference("Eventos").child(eventoID);
        eventosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Evento evento = snapshot.getValue(Evento.class);
                    exibirDetalhesDoEvento(evento);
                    carregarParticipantes(eventoID);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Trate o erro, se necessário
            }
        });
    }

    private void carregarParticipantes(String eventoID) {
        DatabaseReference participantesRef = FirebaseDatabase.getInstance().getReference("Eventos")
                .child(eventoID)
                .child("participantes");

        participantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                participantes.clear();
                for (DataSnapshot participanteSnapshot : snapshot.getChildren()) {
                    String participanteID = participanteSnapshot.getKey();
                    Participante participante = new Participante(participanteID, "");
                    participantes.add(participante);
                    buscaNomeParticipante(participanteID, participante);
                }
                participanteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Trate o erro, se necessário
            }
        });
    }

    private void buscaNomeParticipante(String participanteID, final Participante participante) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(participanteID);
        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome = snapshot.child("Username").getValue(String.class);
                    participante.setNome(nome);
                    participanteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Trate o erro, se necessário
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


    private void adicionarParticipante(String eventoID, String participanteID) {
        DatabaseReference eventoRef = FirebaseDatabase.getInstance().getReference("Eventos").child(eventoID).child("participantes");
        eventoRef.child(participanteID).setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Entrou no evento com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Toast.makeText(getApplicationContext(), "Erro ao adicionar participante: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}