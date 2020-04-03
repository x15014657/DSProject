package com.conorjc.dsproject.server;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



public class ServerTest {

    @BeforeMethod
    public void setUp() {
        System.out.println("Opening Server");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Closing Server");
    }

    @Test(priority = 0)
    public void testServer() {
        System.out.println("Testing Server");
       Assert.assertEquals(1,2);
    }
}