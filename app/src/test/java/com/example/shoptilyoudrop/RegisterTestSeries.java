package com.example.shoptilyoudrop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

/**
 *
 * Tests for registering.
 *
 */
@RunWith(RobolectricTestRunner.class)
public class RegisterTestSeries {
    RegisterActivity register;

    @Before
    public void setup() {
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
        register.getRegister().performClick();

        assertEquals("Failed", register.getToastMessage());
    }
}
