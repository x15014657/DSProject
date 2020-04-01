package com.conorjc.dsproject.server;


import com.proto.thermo.Thermo;
import com.proto.thermo.ThermoRequest;
import com.proto.thermo.ThermoResponse;
import com.proto.thermo.ThermoServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ThermoServiceImpl extends ThermoServiceGrpc.ThermoServiceImplBase {

    @Override
    public void thermoStatus(ThermoRequest request, StreamObserver<ThermoResponse> responseObserver) {
        Thermo thermoStatus = request.getStatus();
        Boolean status = thermoStatus.getStatus();

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

}
