package com.example.hubbyandroid.interfaces;

import com.google.firebase.auth.FirebaseUser;

public interface LoginCallback {
    void onLoginSuccess(FirebaseUser user);
    void onLoginFailure();
}
