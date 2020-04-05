package com.conorjc.dsproject.impl;

import com.proto.thermo.*;
import io.grpc.stub.StreamObserver;

public class ThermoServiceImpl extends ThermoServiceGrpc.ThermoServiceImplBase {

    @Override
    public void thermoStatus(ThermoRequest request, StreamObserver<ThermoResponse> responseObserver) {
        Thermo thermoStatus = request.getStatus();
        boolean status = thermoStatus.getStatus();

        //create the  response
        String result;
        if(!status){
            result = "The Thermostat is Offline";
        }else{
            result = "The Thermostat is Online";
        }

        ThermoResponse response = ThermoResponse.newBuilder()
                .setResult(result)
                .build();

        //sends response to client
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();

    }

    @Override
    public void heatUpService(HeatUpRequest request, StreamObserver<HeatUpResponse> responseObserver) {

        Thermo warm = request.getStatus();
        boolean stat = warm.getStatus();

        //create the action before the response
        if (!stat) {
            try {
                for (int i = 1; i < 2; i++) {
                    String level1 = (i + "Setting Level 1");
                    String level2 = (i + "Setting Level 2");
                    String level3 = (i + "Setting Level 3");

                    HeatUpResponse response = HeatUpResponse.newBuilder()
                            .setLevel1(level1)
                            .setLevel2(level2)
                            .setLevel3(level3)
                            .build();

                    responseObserver.onNext(response);
                    Thread.sleep(1L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("System is offline");
            }
        }
        String result = ("Thermostat set to Warmest Setting.");
        HeatUpResponse response = HeatUpResponse.newBuilder()
                .setResult(result)
                .build();
        responseObserver.onNext(response);
    }
}