package com.conorjc.dsproject.server;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PrinterServiceTest {

    @BeforeMethod
    public void setUp() {
        System.out.println("Opening Printer test");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Closing Printer Test");
    }

    @Test
    public void testPrinterStatus() {
        System.out.println("Testing Printer Status");
    }

    @Test
    public void testCheckPrinter() {
        System.out.println("Testing Printer Tests");
    }
}