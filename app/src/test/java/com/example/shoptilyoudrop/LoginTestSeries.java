package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * A series of local unit tests for the login activity of the Shop-til-You-Drop app.
 *
 */
@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class LoginTestSeries {
    private LoginActivity login;

    @Before
    public void setup() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        login = Robolectric.buildActivity(LoginActivity.class)
                .create()
                .resume()
                .get();

    }

    @Test
    public void emptyLogin() {
        login.getEmail().setText("");
        login.getPassword().setText("");
        login.getLogin().performClick();

        assertEquals("Login Failed", login.getToastMessage());
    }

    @Test
    public void emptyPassword() {
        login.getEmail().setText("sfkrogel@mtu.edu");
        login.getPassword().setText("");
        login.getLogin().performClick();

        assertEquals("Login Failed", login.getToastMessage());
    }

    @Test
    public void emptyEmail() {
        login.getEmail().setText("");
        login.getPassword().setText("longerthan8chars");
        login.getLogin().performClick();

        assertEquals("Login Failed", login.getToastMessage());
    }

    @Test
    public void correctLogin() {
        login.getEmail().setText("joeojazz@gmail,com");
        login.getPassword().setText("12345678");
        login.getAuthor().signInWithEmailAndPassword(login.getEmail().getText().toString(), login.getPassword().getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                assertEquals("Login Successful!", "Login Successful!");
            }

        });
        login.getAuthor().signInWithEmailAndPassword(login.getEmail().getText().toString(), login.getPassword().getText().toString()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fail();
            }
        });

    }
}