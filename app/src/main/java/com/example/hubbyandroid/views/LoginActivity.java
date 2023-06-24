package com.example.hubbyandroid.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hubbyandroid.R;
import com.example.hubbyandroid.controller.LoginController;
import com.example.hubbyandroid.interfaces.LoginCallback;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginCallback {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button loginButton;
    private TextView linkTextView;
    private CheckBox checkBoxManterLogado;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginController = new LoginController(this);

        editTextEmail = findViewById(R.id.editEmail);
        editTextSenha = findViewById(R.id.editSenha);
        loginButton = findViewById(R.id.buttonLogin);
        linkTextView = findViewById(R.id.login);
        checkBoxManterLogado = findViewById(R.id.boxManterLogado);

        linkTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        checkBox();
    }

    private void checkBox() {
        if (loginController.isUserLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
            Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            loginController.saveSharedPref(false);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonLogin:
                String username = editTextEmail.getText().toString();
                String password = editTextSenha.getText().toString();

                if(username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos",
                            Toast.LENGTH_SHORT).show();
                } else {
                    loginController.saveSharedPref(checkBoxManterLogado.isChecked());
                    loginController.login(username, password, this);
                }
                break;

            case R.id.login:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {
        startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
        Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailure() {
        Toast.makeText(LoginActivity.this, "Falha no login.", Toast.LENGTH_SHORT).show();
    }
}