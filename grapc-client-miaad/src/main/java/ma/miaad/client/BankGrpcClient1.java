package ma.miaad.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.miaad.stubs.Bank;
import ma.miaad.stubs.BankServiceGrpc;

public class BankGrpcClient1 {
    public static void main(String[] args) {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",8989)
                .usePlaintext()
                .build();

        BankServiceGrpc.BankServiceBlockingStub BlockingStub=BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest request=Bank.ConvertCurrencyRequest.newBuilder()
                .setAmount(7899).
                setCurrencyFrom("DH")
                .setCurrencyTo("EUR")
                .build();
        Bank.ConvertCurrencyResponse convertResponse = BlockingStub.convert(request);
        System.out.println(convertResponse.toString());

    }
}

