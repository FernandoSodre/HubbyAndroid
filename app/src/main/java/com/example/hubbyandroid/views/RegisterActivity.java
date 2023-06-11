package com.example.hubbyandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hubbyandroid.R;
import com.example.hubbyandroid.controller.UsersController;
import com.example.hubbyandroid.models.Users;

public class RegisterActivity extends AppCompatActivity {

    Users users;

    UsersController usersController;

    EditText editUsuarioCadastro;
    EditText editEmailCadastro;
    EditText editSenhaCadastro;
    EditText editSenhaCadastro2;
    Button editBtnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usersController = new UsersController(RegisterActivity.this);
        usersController.toString();
        users = new Users();

        editUsuarioCadastro = findViewById(R.id.editUsuarioCadastro);
        editEmailCadastro = findViewById(R.id.editEmailCadastro);
        editSenhaCadastro = findViewById(R.id.editSenhaCadastro);
        editSenhaCadastro2 = findViewById(R.id.editSenhaCadastro2);
        editBtnCadastrar = findViewById(R.id.editBtnCadastrar);

        editBtnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.setNickname(editUsuarioCadastro.getText().toString());
                users.setEmail(editEmailCadastro.getText().toString());
                users.setPassword(editSenhaCadastro.getText().toString());

                usersController.salvar(users);
                Log.i("Teste_actv", "salvo" +users.toString());

            }
        });


    }
}