package com.conorjc.dsproject.server;

import com.proto.vpn.Vpn;
import com.proto.vpn.VpnRequest;
import com.proto.vpn.VpnResponse;
import com.proto.vpn.VpnServiceGrpc;
import io.grpc.stub.StreamObserver;

public class VpnServiceImpl extends VpnServiceGrpc.VpnServiceImplBase {

        @Override
        public void vpnStatus(VpnRequest request, StreamObserver<VpnResponse> responseObserver) {
                Vpn vpnStatus = request.getStatus();
                boolean status = vpnStatus.getStatus();

        //create the  response
        String result;
        if(!status){
        result = "The Vpn is Offline";
        }else{
        result = "The Vpn is Online";
        }

        VpnResponse response = VpnResponse.newBuilder()
        .setResult(result)
        .build();

        //sends response to client
        responseObserver.onNext(response);

        //complete the RPC call
        responseObserver.onCompleted();

        }

}
