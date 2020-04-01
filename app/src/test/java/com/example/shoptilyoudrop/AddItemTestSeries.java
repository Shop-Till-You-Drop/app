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
 * A series of local unit tests for the main activity that allows adding items of the Shop-til-You-Drop app.
 *
 */
@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class AddItemTestSeries {
    private MainActivity prime;

    @Before
    public void setup() {
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        prime = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void addItem() {
        prime.setfName("sfkrogel@mtu,edu");

        prime.getFood().setText("Strawberries");
        prime.getStore().setText("Econo");
        prime.getPrice().setText("2.67");

        prime.findViewById(R.id.add).performClick();

        assertEquals("App failed to give the message of an item being added to the account. Recheck the logic behind adding an item or specifying an account.", "Added to sfkrogel@mtu,edu", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void emptyItemTest() {
        prime.setfName("sfkrogel@mtu,edu");

        prime.getFood().setText("");
        prime.getStore().setText("Walmart");
        prime.getPrice().setText("2.67");

        prime.findViewById(R.id.add).performClick();

        assertEquals("App did not report that the food was invalid. Check for what might ignore the food name text.","Invalid Food", ShadowToast.getTextOfLatestToast());

    }
    @Test
    public void emptyStoreTest() {
        prime.setfName("sfkrogel@mtu,edu");

        prime.getFood().setText("Blueberries");
        prime.getStore().setText("");
        prime.getPrice().setText("2.67");

        prime.findViewById(R.id.add).performClick();

        assertEquals("App did not report that the food was invalid. Check for what might ignore the store name text.","Invalid Store", ShadowToast.getTextOfLatestToast());
    }
    @Test
    public void emptyPriceTest() {
        prime.setfName("sfkrogel@mtu,edu");

        prime.getFood().setText("Blueberries");
        prime.getStore().setText("Walmart");
        prime.getPrice().setText("");

        prime.findViewById(R.id.add).performClick();

        assertEquals("App did not report that the food was invalid. Check for what might ignore the price text.","Invalid Price", ShadowToast.getTextOfLatestToast());
    }



}
