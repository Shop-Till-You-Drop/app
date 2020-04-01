package com.example.shoptilyoudrop;

import androidx.test.core.app.ApplicationProvider;
import com.google.firebase.FirebaseApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

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

        assertEquals( "Login did not return as failed. It may have crashed or been allowed to succeed. Check for how an empty username and empty password could continue on.",
                "Login Failed", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void emptyPassword() {
        login.getEmail().setText("WhatAn@email.perfect");
        login.getPassword().setText("");
        login.getLogin().performClick();

        assertEquals("Login did not return as failed. It may have crashed or been allowed to succeed. Check for how an empty password could continue on.", "Login Failed", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void emptyEmail() {
        login.getEmail().setText("");
        login.getPassword().setText("TotallyAPassword");
        login.getLogin().performClick();

        assertEquals("Login did not return as failed. It may have crashed or been allowed to succeed. Check for how an empty username could continue on.","Login Failed", ShadowToast.getTextOfLatestToast());
    }
}