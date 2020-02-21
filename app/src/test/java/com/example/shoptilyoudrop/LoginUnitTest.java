package com.example.shoptilyoudrop;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginUnitTest {
/**    @Test
    public void emptyLogin() {
        LoginActivity login = new LoginActivity();
        login.getEmail().setText("");
        login.getPassword().setText("");
        login.getLogin().performClick();

        assertEquals("Empty", login.getToastMessage());
    }

    @Test
    public void smallPassword() {
        RegisterActivity register = new RegisterActivity();
        register.getEmail().setText("sfkrogel@mtu.edu");
        register.getPassword().setText("123");
        register.getRegister().performClick();

        assertEquals("Password must be 8 characters", register.getToastMessage());
    }

    @Test
    public void failedMessage() {
        RegisterActivity register = new RegisterActivity();
        register.getEmail().setText("sfkrogelnotemail");
        register.getPassword().setText("longerthan8chars");
        register.getRegister().performClick();

        assertEquals("Failed", register.getToastMessage());
    } */
}