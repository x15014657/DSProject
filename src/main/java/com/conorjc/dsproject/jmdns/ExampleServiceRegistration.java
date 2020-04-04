
package com.conorjc.dsproject.jmdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

// This code is adapted from https://github.com/jmdns/jmdns
public class ExampleServiceRegistration {

    public static void main(String[] args) throws InterruptedException {

        try {
            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            // Register a service
            ServiceInfo serviceInfo = ServiceInfo.create("_printerServiceImpl._tcp.local.", "PrinterService", 5000, "com/impl/");
            jmdns.registerService(serviceInfo);
            ServiceInfo serviceInfo1 = ServiceInfo.create("_vpnServiceImpl._tcp.local.", "VpnService", 5001, "com/impl/");
            jmdns.registerService(serviceInfo1);
            ServiceInfo serviceInfo2 = ServiceInfo.create("_thermoServiceImpl._tcp.local.", "ThermoService", 5001, "com/impl/");
            jmdns.registerService(serviceInfo2);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
