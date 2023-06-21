package com.example.hubbyandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hubbyandroid.R;
import com.example.hubbyandroid.controller.Evento;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsviewHolder>{

    Context context;
    ArrayList<Evento> list;

    public EventsAdapter(Context context, ArrayList<Evento> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventsviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_events,parent, false);
        return new EventsviewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventsviewHolder holder, int position) {

        Evento evento = list.get(position);
        holder.id.setText(evento.getId());
        holder.title.setText(evento.getTitulo());
        holder.dateEvent.setText(evento.getData());
        holder.timeEvent.setText(evento.getHora());
        holder.localEvent.setText(evento.getLocal());
        holder.descriptionEvent.setText(evento.getDescricao());
        holder.categoryEvent.setText(evento.getCategoria());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class EventsviewHolder extends RecyclerView.ViewHolder{

        TextView id, title, dateEvent, timeEvent, localEvent, descriptionEvent, categoryEvent;

        public EventsviewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.idEvento);
            title = itemView.findViewById(R.id.nomeEvento);
            dateEvent = itemView.findViewById(R.id.dateEvent);
            timeEvent = itemView.findViewById(R.id.timeEvent);
            localEvent = itemView.findViewById(R.id.localEvent);
            descriptionEvent = itemView.findViewById(R.id.descriptionEvent);
            categoryEvent = itemView.findViewById(R.id.categoryEvent);
        }
    }
}