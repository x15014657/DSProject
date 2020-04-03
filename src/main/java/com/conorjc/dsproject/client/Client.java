package com.conorjc.dsproject.client;

import com.proto.greet.*;
import com.proto.greet.GreetServiceGrpc;
import com.proto.printer.*;
import com.proto.thermo.Thermo;
import com.proto.thermo.ThermoRequest;
import com.proto.thermo.ThermoResponse;
import com.proto.thermo.ThermoServiceGrpc;
import com.proto.vpn.Vpn;
import com.proto.vpn.VpnRequest;
import com.proto.vpn.VpnResponse;
import com.proto.vpn.VpnServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;



public class Client {



    public static void main(String[] args) {


        System.out.println("Client Interface Initialising...");

        System.out.println("Building Channels...");
        ManagedChannel  channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        ManagedChannel  channel1 = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        ManagedChannel  channel2 = ManagedChannelBuilder.forAddress("localhost", 50053)
                .usePlaintext()
                .build();

        ManagedChannel  channel3 = ManagedChannelBuilder.forAddress("localhost", 50054)
                .usePlaintext()
                .build();


//-------------------------------------------------------------------------------------//
        System.out.println("Creating stubs...");
        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        PrintServiceGrpc.PrintServiceBlockingStub printClient = PrintServiceGrpc.newBlockingStub(channel1);
        VpnServiceGrpc.VpnServiceBlockingStub vpnClient = VpnServiceGrpc.newBlockingStub(channel2);
        ThermoServiceGrpc.ThermoServiceBlockingStub thermoClient = ThermoServiceGrpc.newBlockingStub(channel3);


//-------------------------------------------------------------------------------------//
        System.out.println("Building Protocol Buffer Messages...");
        //created a protocol buffer greeting message
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("Conor")
                .setLastName("Clarke")
                .build();

        Printer printStatus = Printer.newBuilder()
                .setStatus(false)
                .build();

        Vpn vpnStatus = Vpn.newBuilder()
                .setStatus(false)
                .build();

        Thermo thermoStatus = Thermo.newBuilder()
                .setStatus(false)
                .build();

//-------------------------------------------------------------------------------------//
        System.out.println("Building Protocol Buffer Requests...");
        //do the same for a GreetRequest
        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        //Unary Request
        PrinterRequest printerRequest = PrinterRequest.newBuilder()
                .setStatus(printStatus)
                .build();

        //Server Streaming Side
        CheckPrinterRequest checkPrinterRequest =
                CheckPrinterRequest.newBuilder()
                        .setStatus(CheckPrinter.newBuilder().setStatus(true))
                        .build();

        VpnRequest vpnRequest = VpnRequest.newBuilder()
                .setStatus(vpnStatus)
                .build();

        ThermoRequest thermoRequest = ThermoRequest.newBuilder()
                .setStatus(thermoStatus)
                .build();


//-------------------------------------------------------------------------------------//
        System.out.println("Smart HomeOffice Active...");
        //call the RPC and get back a Response (protocol buffers)


        GreetResponse greetResponse = greetClient.greet(greetRequest);
        //Unary
        PrinterResponse printResponse = printClient.printerStatus(printerRequest);
        //Server Streaming
        printClient.checkPrinter(checkPrinterRequest)
                .forEachRemaining(checkPrinterResponse -> {
                    System.out.println(checkPrinterResponse.getNetwork());
                    System.out.println(checkPrinterResponse.getCartridge());
                    System.out.println(checkPrinterResponse.getInk());
                    System.out.println(checkPrinterResponse.getResult());
                });

        VpnResponse vpnResponse = vpnClient.vpnStatus(vpnRequest);

        ThermoResponse thermoResponse = thermoClient.thermoStatus(thermoRequest);

        //Print the response from the server
        System.out.println(greetResponse.getResult());
        System.out.println(printResponse.getResult());
        System.out.println(vpnResponse.getResult());
        System.out.println(thermoResponse.getResult());

        //Shut our channels down
        //System.out.println("Shutting down channels...");
        // channel.shutdown();
        // channel1.shutdown();
        // channel2.shutdown();
        // channel3.shutdown();
        // }

    }
}