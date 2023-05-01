package ma.miaad.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ma.miaad.service.BankGrpcService;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException,InterruptedException {
        Server server= ServerBuilder.forPort(8989)
                .addService(new BankGrpcService())
                .build();
        server.start();
        //
        server.awaitTermination();
    }
}
