package com.example.hubbyandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hubbyandroid.R;
import com.example.hubbyandroid.models.Participante;

import java.util.List;

public class ParticipanteAdapter extends RecyclerView.Adapter<ParticipanteAdapter.ViewHolder> {

    private List<Participante> participantes;

    public ParticipanteAdapter(List<Participante> participantes) {
        this.participantes = participantes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.textNome);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participante, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Participante participante = participantes.get(position);
        holder.nomeTextView.setText(participante.getNome());
    }

    @Override
    public int getItemCount() {
        return participantes.size();
    }
}

