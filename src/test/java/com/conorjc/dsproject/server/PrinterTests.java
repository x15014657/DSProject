package com.conorjc.dsproject.server;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PrinterTests {

    @BeforeMethod
    public void setUp() {
        System.out.println("Opening Printer Tests");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Printer Tests Closing");
    }

    @Test
    public void testPrinterStatus() {
        System.out.println("Testing Printer Status");
    }

    @Test
    public void testCheckPrinter() {
        System.out.println("Testing Printer Self-Tests");
    }

    @Test
    public void testLongPrintTest() {
        System.out.println("Testing Print Test Page Test");
    }
}