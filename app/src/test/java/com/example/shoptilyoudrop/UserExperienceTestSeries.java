package com.example.shoptilyoudrop;

import android.content.Intent;

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
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * A series of unit tests to make sure that the user can navigate the app as expected.
 *
 */
@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class UserExperienceTestSeries {

    private StartActivity start;
    private RegisterActivity register;
    private LoginActivity login;
    private MainActivity prime;

    @Before
    public void setup() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void StartToRegister(){
        start = Robolectric.buildActivity(StartActivity.class)
                .create()
                .resume()
                .get();
        start.findViewById(R.id.register).performClick();
        Intent expected = new Intent(start, RegisterActivity.class);
        Intent actual = Shadows.shadowOf(start).getNextStartedActivity();
        assertEquals("Start failed to reach RegisterActivity. Check logic behind the button meant to bring the user to the RegisterActivity.", expected.toString().trim(), actual.toString().trim());
    }

    @Test
    public void StartToLogin(){
        start = Robolectric.buildActivity(StartActivity.class)
                .create()
                .resume()
                .get();
        start.findViewById(R.id.login).performClick();
        Intent expected = new Intent(start, LoginActivity.class);
        Intent actual = Shadows.shadowOf(start).getNextStartedActivity();
        assertEquals("Start failed to reach LoginActivity. Check logic behind the button meant to bring the user to the LoginActivity.", expected.toString().trim(), actual.toString().trim());
    }

}
