package com.example.hubbyandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hubbyandroid.R;
import com.example.hubbyandroid.controller.UsersController;
import com.example.hubbyandroid.models.Users;

public class LoginActivity extends AppCompatActivity {

    Users users;

    UsersController usersController;

    EditText editUsarioLogin;
    EditText editSenhaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn= findViewById(R.id.editRegistrar);


        usersController = new UsersController(LoginActivity.this);
        users = new Users();
        users = usersController.buscar(users);
        editUsarioLogin = findViewById(R.id.editUsarioLogin);
        editSenhaLogin = findViewById(R.id.editSenhaLogin);

        //users.setNickname(editUsarioLogin.getText().toString());
        //users.setPassword(editSenhaLogin.getText().toString());

        editUsarioLogin.setText(users.getNickname());
        editSenhaLogin.setText(users.getPassword());

    }
}