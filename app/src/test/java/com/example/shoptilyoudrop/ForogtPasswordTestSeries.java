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
 * A series of local unit tests for the forgot password activity that allows a user to reset their password with an email link.
 *
 */
@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class ForogtPasswordTestSeries {
    private ForgotPasswordActivity forgot;

    @Before
    public void setup() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        forgot = Robolectric.buildActivity(ForgotPasswordActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void emptyEmailTest() {
        forgot.getUserEmail().setText("");
        forgot.getResetLink().performClick();

        assertEquals("An empty email address was not stopped before attempting to send a reset link. Recheck the login in the resetLink button.", "Email was empty. Please try again.", ShadowToast.getTextOfLatestToast());
    }
}
