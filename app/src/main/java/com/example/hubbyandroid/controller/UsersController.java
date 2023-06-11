package com.example.hubbyandroid.controller;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.hubbyandroid.models.Users;
import com.example.hubbyandroid.views.RegisterActivity;

public class UsersController {
    SharedPreferences preferences;
    SharedPreferences.Editor cadastro_user;

    public static final String NOME_PREFERENCES = "pref_cadastro";

    public UsersController(RegisterActivity registerActivity){
        preferences = registerActivity.getSharedPreferences(NOME_PREFERENCES,0);
        cadastro_user = preferences.edit();
    }
     @Override
    public String toString() {
        return super.toString();
    }


    public void salvar(Users users){
        Log.i("Controller", "Salvo: " +users.toString());
        cadastro_user.putString("nome completo", users.getNickname());
        cadastro_user.putString("email", users.getEmail());
        cadastro_user.putString("senha", users.getPassword());
        cadastro_user.apply();
    };

    public Users buscar(Users users){
        users.setNickname(preferences.getString("nome completo"," "));
        users.setEmail(preferences.getString("email", " "));
        users.setPassword(preferences.getString("senha", " "));
        return users;
    };



}
