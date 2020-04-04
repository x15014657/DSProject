package com.conorjc.dsproject.impl;

import com.proto.printer.*;
import com.proto.thermo.Thermo;
import com.proto.thermo.ThermoRequest;
import com.proto.thermo.ThermoResponse;
import com.proto.thermo.ThermoServiceGrpc;
import com.proto.vpn.Vpn;
import com.proto.vpn.VpnServiceGrpc;
import com.proto.vpn.VpnStatusRequest;
import com.proto.vpn.VpnStatusResponse;
import io.grpc.stub.StreamObserver;

public class PrinterServiceImpl extends PrintServiceGrpc.PrintServiceImplBase {

    public void printerStatus(PrinterStatusRequest request, StreamObserver<PrinterStatusResponse> responseObserver) {
        Printer printStatus = request.getStatus();
        boolean status = printStatus.getStatus();

        //create the  response
        String result;
        if (!(status)) {
            result = "The Printer is Offline";
        } else {
            result = "The Printer is Online";
        }

        PrinterStatusResponse response = PrinterStatusResponse.newBuilder()
                .setResult(result)
                .build();

        //sends response to client
        responseObserver.onNext(response);
        //complete the RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void checkPrinter(CheckPrinterRequest request, StreamObserver<CheckPrinterResponse> responseObserver) {
        Printer checkPrinter = request.getStatus();
        boolean status = checkPrinter.getStatus();

        //create the action before the response
        if (status) {
            try {
                for (int i = 1; i < 4; i++) {
                    String network = ("Network Test " + i + " of 3");
                    String cartridge = ("Cartridge Test " + i + " of 3");
                    String ink = ("Ink Test " + i + " of 3");

                    CheckPrinterResponse response = CheckPrinterResponse.newBuilder()
                            .setNetwork(network)
                            .setCartridge(cartridge)
                            .setInk(ink)
                            .build();

                    responseObserver.onNext(response);
                    Thread.sleep(1L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("System is offline");
            }
        }
        String result = ("Network Connected: 192.168.0.1, " + " Ink Levels: Good, " + "Cartridges: Aligned. ");
        CheckPrinterResponse response = CheckPrinterResponse.newBuilder()
                .setResult(result)
                .build();
        responseObserver.onNext(response);
    }

    @Override
    public StreamObserver<LongPrintTestRequest> longPrintTest(StreamObserver<LongPrintTestResponse> responseObserver) {
        return new StreamObserver<LongPrintTestRequest>() {
            String result = "";

            @Override
            public void onNext(LongPrintTestRequest value) {
                //client sends a message
                result += "" + value.getTp().getTestpage();
            }

            @Override
            public void onError(Throwable t) {
                //client sends an error
            }

            @Override
            public void onCompleted() {
                //client is done
                responseObserver.onNext(
                        LongPrintTestResponse.newBuilder()
                                .setResult(result)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
    }


}