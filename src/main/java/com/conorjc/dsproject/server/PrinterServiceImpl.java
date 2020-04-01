package com.conorjc.dsproject.server;

import com.proto.printer.PrintServiceGrpc;
import com.proto.printer.Printer;
import com.proto.printer.PrinterRequest;
import com.proto.printer.PrinterResponse;
import io.grpc.stub.StreamObserver;

public class PrinterServiceImpl extends PrintServiceGrpc.PrintServiceImplBase {

    @Override
    public void printerStatus(PrinterRequest request, StreamObserver<PrinterResponse> responseObserver) {
        Printer printStatus = request.getStatus();
        Boolean status = printStatus.getStatus();


        //create the  response
        String result;
        if(status != true){
           result = "The Printer is Offline";
        }else{
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

}
