package com.conorjc.dsproject.client;

import com.conorjc.dsproject.jmdns.GetRequest;
import com.proto.printer.*;
import com.proto.thermo.Thermo;
import com.proto.thermo.ThermoRequest;
import com.proto.thermo.ThermoResponse;
import com.proto.thermo.ThermoServiceGrpc;
import com.proto.vpn.Vpn;
import com.proto.vpn.VpnServiceGrpc;
import com.proto.vpn.VpnStatusRequest;
import com.proto.vpn.VpnStatusResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class Client {

    public void run() {

        System.out.println("Client Interface Initialising...");
        System.out.println("Building Channels...");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5000)
                .usePlaintext()
                .build();
        ManagedChannel channel1 = ManagedChannelBuilder.forAddress("localhost", 5001)
                .usePlaintext()
                .build();
        ManagedChannel channel2 = ManagedChannelBuilder.forAddress("localhost", 5002)
                .usePlaintext()
                .build();

        // Comment & Un-Comment To Use Different Streams

        doUnaryCall(channel, channel1, channel2);
        doServerStreamingCall(channel);
        //doClientStreamingCall(channel);


       /* System.out.println("Shutting Down Channels");
        channel.shutdown();
        channel.shutdown();
        channel2.shutdown();
        channel3.shutdown();*/

    }

    private void doUnaryCall(ManagedChannel channel, ManagedChannel channel1, ManagedChannel channel2) {

        /*------------------------Printer Status---------------------------------------------------------*/

        PrintServiceGrpc.PrintServiceBlockingStub printClient = PrintServiceGrpc.newBlockingStub(channel);

        Printer printerStatus = Printer.newBuilder()
                .setStatus(true)
                .build();

        PrinterStatusRequest printerStatusRequest = PrinterStatusRequest.newBuilder()
                .setStatus(printerStatus)
                .build();

        PrinterStatusResponse printerStatusResponse = printClient.printerStatus(printerStatusRequest);

        System.out.println(printerStatusResponse.getResult());



        /*------------------------Vpn Status---------------------------------------------------------*/

        VpnServiceGrpc.VpnServiceBlockingStub vpnClient = VpnServiceGrpc.newBlockingStub(channel1);

        Vpn vpnStatus = Vpn.newBuilder()
                .setStatus(false)
                .build();

        VpnStatusRequest vpnStatusRequest = VpnStatusRequest.newBuilder()
                .setStatus(vpnStatus)
                .build();

        VpnStatusResponse vpnStatusResponse = vpnClient.vpnStatus(vpnStatusRequest);

        System.out.println(vpnStatusResponse.getResult());



        /*------------------------ThermoStat Status---------------------------------------------------------*/

        ThermoServiceGrpc.ThermoServiceBlockingStub thermoClient = ThermoServiceGrpc.newBlockingStub(channel2);
        Thermo thermoStatus = Thermo.newBuilder()
                .setStatus(false)
                .build();
        ThermoRequest thermoRequest = ThermoRequest.newBuilder()
                .setStatus(thermoStatus)
                .build();

        ThermoResponse thermoResponse = thermoClient.thermoStatus(thermoRequest);
        System.out.println(thermoResponse.getResult());


    }

    private void doServerStreamingCall(ManagedChannel channel) {

        /*------------------------Check Printer ---------------------------------------------------------*/

        PrintServiceGrpc.PrintServiceBlockingStub printClient = PrintServiceGrpc.newBlockingStub(channel);

        //Server Streaming Side
        CheckPrinterRequest checkPrinterStatusRequest =
                CheckPrinterRequest.newBuilder()
                        .setStatus(Printer.newBuilder()
                                .setStatus(true))
                        .build();

        //Server Streaming
        printClient.checkPrinter(checkPrinterStatusRequest)
                .forEachRemaining(checkPrinterResponse -> {
                    System.out.println(checkPrinterResponse.getNetwork());
                    System.out.println(checkPrinterResponse.getCartridge());
                    System.out.println(checkPrinterResponse.getInk());
                    System.out.println(checkPrinterResponse.getResult());
                });



        /*-----------------------------------------------------------------------------------------------*/
    }

    private void doClientStreamingCall(ManagedChannel channel) {

        PrintServiceGrpc.PrintServiceStub asyncClient = PrintServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LongPrintTestRequest> requestObserver = asyncClient.longPrintTest(new StreamObserver<LongPrintTestResponse>() {
            @Override
            public void onNext(LongPrintTestResponse value) {
                //we get a response from the server
                System.out.println("Received Response from the server");
                System.out.println(value.getResult());
                //onNext will only be called once
            }

            @Override
            public void onError(Throwable t) {
                //we get an error from the server
            }

            @Override
            public void onCompleted() {
                //the server is done sending us data
                //onCompleted will be called right after onNext()
                System.out.println("Server has completed sending data");
                latch.countDown();
            }
        });
        //Sending Message 1
        requestObserver.onNext(LongPrintTestRequest.newBuilder()
                .setTp(Printer.newBuilder()
                        .setTestpage("\nThis Is The Test Page")
                        .build())
                .build());

        //Sending Message 2
        requestObserver.onNext(LongPrintTestRequest.newBuilder()
                .setTp(Printer.newBuilder()
                        .setTestpage("\nIf this page is visible to\n" +
                                "you then you are right in \n" +
                                "presuming that the printer\n" +
                                "is working correctly!!!   \n" +
                                "White Ink Test Complete   \n")
                        .build())
                .build());

        //Sending Message 3
        requestObserver.onNext(LongPrintTestRequest.newBuilder()
                .setTp(Printer.newBuilder()
                        .setTestpage("End of TestPage \n")
                        .build())
                .build());

        //we tell the server that that client is done sending data
        requestObserver.onCompleted();
        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        channel.shutdown();

    }


    public static void main(String[] args) throws IOException {
        Client main = new Client();
        main.run();

        try {
            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            // Add a service listener
            jmdns.addServiceListener("_printerServiceImpl._tcp.local.", new SampleListener());

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private static class SampleListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            System.out.println("Service added: " + event.getInfo());
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            System.out.println("Service removed: " + event.getInfo());
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            ServiceInfo info = event.getInfo();
            int port = info.getPort();
            String path = info.getNiceTextString().split("=")[1];
            GetRequest.request("localhost:" + port + "/" + path);
        }
    }
}



