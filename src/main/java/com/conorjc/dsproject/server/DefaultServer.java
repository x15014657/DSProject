package com.conorjc.dsproject.server;


import io.grpc.ServerBuilder;
import java.io.IOException;
public class DefaultServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Servers Initialising...");


        System.out.println("Adding services...");
        //build our server and assign a port
        io.grpc.Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .build();

        io.grpc.Server server1 = ServerBuilder.forPort(50052)
                .addService(new PrinterServiceImpl())
                .build();

        io.grpc.Server server2 = ServerBuilder.forPort(50053)
                .addService(new VpnServiceImpl())
                .build();

        io.grpc.Server server3 = ServerBuilder.forPort(50054)
                .addService(new ThermoServiceImpl())
                .build();

        //start our server
        server.start();
        server1.start();
        server2.start();
        server3.start();
        System.out.println("Servers Initialised");

        //allow our server to accept a request to shutdown, this needs to go before termination
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Received Shutdown Requests");
            server.shutdown();
            server1.shutdown();
            server2.shutdown();
            server3.shutdown();
            System.out.println("Successfully Shutdown Servers");
        } ));

        server.awaitTermination();
        server1.awaitTermination();
        server2.awaitTermination();
        server3.awaitTermination();
    }

}
