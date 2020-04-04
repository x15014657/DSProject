package com.conorjc.dsproject.server;


import com.conorjc.dsproject.impl.PrinterServiceImpl;
import com.conorjc.dsproject.impl.ThermoServiceImpl;
import com.conorjc.dsproject.impl.VpnServiceImpl;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Servers Initialising...");


        System.out.println("Adding services...");


        io.grpc.Server server =
                ServerBuilder.forPort(5000)
                .addService(new PrinterServiceImpl())
                .build();

        try
        {
            server.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        io.grpc.Server server1 =
        ServerBuilder.forPort(5001)
                .addService(new VpnServiceImpl())
                .build();


        try
        {
            server1.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        io.grpc.Server server2 =
        ServerBuilder.forPort(5002)
                .addService(new ThermoServiceImpl())
                .build();

        try
        {
            server2.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        System.out.println("Server Initialised");

        //allow our server to accept a request to shutdown, this needs to go before termination
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Received Shutdown Requests");
            //server.shutdown();
            //server1.shutdown();
           // server2.shutdown();

            System.out.println("Successfully Shutdown Servers");
        } ));

        server.awaitTermination();

    }

}