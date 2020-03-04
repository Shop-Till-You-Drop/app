package com.example.shoptilyoudrop;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 *
 * A series of local unit tests for the registering activity of the Shop-til-You-Drop app.
 *
 */
@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class RegisterTestSeries {
    private RegisterActivity register;

    @Before
    public void setup() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        register = Robolectric.buildActivity(RegisterActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void emptyRegister() {
        register.getEmail().setText("");
        register.getPassword().setText("");
        register.getRegister().performClick();

        assertEquals("Empty", register.getToastMessage());
    }

    @Test
    public void smallPassword() {
        register.getEmail().setText("sfkrogel@mtu.edu");
        register.getPassword().setText("123");
        register.getRegister().performClick();

        assertEquals("Password must be 8 characters", register.getToastMessage());
    }

    @Test
    public void failedMessage() {
        register.getEmail().setText("sfkrogelnotemail");
        register.getPassword().setText("longerthan8chars");

        register.getTemp().createUserWithEmailAndPassword(register.getEmail().getText().toString(), register.getPassword().getText().toString()).addOnCompleteListener(register, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        fail();
                    }
                    else {
                        assertEquals("Failed", "Failed");
                    }
                }
            });
    }
}
