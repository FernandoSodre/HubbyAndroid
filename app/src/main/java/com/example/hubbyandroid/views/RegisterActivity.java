package com.example.hubbyandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hubbyandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUser;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button registerButton;
    private TextView linkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.editEmail);
        editTextSenha = findViewById(R.id.editSenha);
        registerButton = findViewById(R.id.buttonRegister);
        linkTextView = findViewById(R.id.login);
        editTextUser = findViewById(R.id.editUser);

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUser.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextSenha.getText().toString();

                registrar(email,password,username);
            }
        });

    }
    private FirebaseAuth mAuth;

    public FirebaseAuth getmAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private void registrar(String email, String password, String username){
        getmAuth();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            reference.child("Usuarios").child(user.getUid()).child("Username").setValue(username);

                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();

                            Toast.makeText(RegisterActivity.this, "Cadastrado com sucesso! Faça login com as suas credenciais.",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(RegisterActivity.this, "Falha no cadastro.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}