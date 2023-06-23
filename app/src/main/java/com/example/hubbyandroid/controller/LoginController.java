package com.example.hubbyandroid.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hubbyandroid.interfaces.LoginCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginController {
    public static final String SHARED_PREFS = "shared_pref";
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    public LoginController(Context context) {
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public void login(String email, String password, final LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onLoginSuccess(user);
                        } else {
                            callback.onLoginFailure();
                        }
                    }
                });
    }

    public void saveSharedPref(boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pref_check", checked ? "true" : "");
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        String prefCheck = sharedPreferences.getString("pref_check", "");
        return prefCheck.equals("true");
    }
}
