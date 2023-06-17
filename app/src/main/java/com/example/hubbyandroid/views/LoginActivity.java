package com.example.hubbyandroid.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hubbyandroid.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button loginButton;
    private TextView linkTextView;
    private CheckBox checkBoxManterLogado;
    public static final String SHARED_PREFS = "shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editEmail);
        editTextSenha = findViewById(R.id.editSenha);
        loginButton = findViewById(R.id.buttonLogin);
        linkTextView = findViewById(R.id.login);
        checkBoxManterLogado = findViewById(R.id.boxManterLogado);

        checkBox();

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextEmail.getText().toString();
                String password = editTextSenha.getText().toString();

                login(username,password);
            }
        });
    }

    private void checkBox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String checkBox = sharedPreferences.getString("pref_check", "");
        if(checkBox.equals("true")) {
            startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
            Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!.",
                        Toast.LENGTH_SHORT).show();
            finish();

        } else {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().apply();

        }

    }

    private FirebaseAuth mAuth;

    public FirebaseAuth getmAuth() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    private void login(String email, String password){
        getmAuth();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if(checkBoxManterLogado.isChecked()){
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("pref_check", "true");
                                editor.apply();
                            }
                            startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                            Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!.",
                                    Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(LoginActivity.this, "Falha no login.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}