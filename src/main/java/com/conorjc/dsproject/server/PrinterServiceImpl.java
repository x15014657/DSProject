package com.conorjc.dsproject.server;


import com.proto.printer.*;
import io.grpc.stub.StreamObserver;

public class PrinterServiceImpl extends PrintServiceGrpc.PrintServiceImplBase {

    @Override
    public void printerStatus(PrinterRequest request, StreamObserver<PrinterResponse> responseObserver) {
        Printer printStatus = request.getStatus();
        boolean status = printStatus.getStatus();


        //create the  response
        String result;
        if (!(status)) {
            result = "The Printer is Offline";
        } else {
            result = "The Printer is Online";
        }

        PrinterResponse response = PrinterResponse.newBuilder()
                .setResult(result)
                .build();

        //sends response to client
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();

        // super.greet(request, responseObserver);

    }

    @Override
    public void checkPrinter(CheckPrinterRequest request, StreamObserver<CheckPrinterResponse> responseObserver) {
        CheckPrinter checkPrinter = request.getStatus();
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
                    Thread.sleep(3000L);
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
}
