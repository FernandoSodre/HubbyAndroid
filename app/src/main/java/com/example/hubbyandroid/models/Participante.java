package com.example.hubbyandroid.models;

public class Participante {
    private String id;
    private String nome;

    public Participante() {
        // Construtor vazio necessário para o Firebase
    }

    public Participante(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}