package com.example.hubbyandroid.models;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String id;
    private String titulo;
    private String data;
    private String hora;
    private String local;
    private String descricao;
    private String categoria;
    private List<String> participantes;

    public Evento(){};

    public Evento(String id, String titulo, String data, String hora, String local, String descricao, String categoria) {

        this.titulo = titulo;
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.local = local;
        this.descricao = descricao;
        this.categoria = categoria;
        this.participantes = new ArrayList<>();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

   public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<String> participantes) {
        this.participantes = participantes;
    }

    public void adicionarParticipante(String participanteId) {
        participantes.add(participanteId);
    }

}
