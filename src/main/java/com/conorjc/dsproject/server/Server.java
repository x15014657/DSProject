package com.conorjc.dsproject.server;


import io.grpc.ServerBuilder;
import java.io.IOException;
public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Servers Initialising...");


        System.out.println("Adding services...");


        io.grpc.Server server =
                ServerBuilder.forPort(50050)
                .addService(new PrinterServiceImpl())
                .build();

        ServerBuilder.forPort(50051)
                .addService(new VpnServiceImpl())
                .build();

        ServerBuilder.forPort(50052)
                .addService(new ThermoServiceImpl())
                .build();

        //start our server

        server.start();
        System.out.println("Server Initialised");

        //allow our server to accept a request to shutdown, this needs to go before termination
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Received Shutdown Requests");
            server.shutdown();

            System.out.println("Successfully Shutdown Servers");
        } ));

        server.awaitTermination();

    }

}