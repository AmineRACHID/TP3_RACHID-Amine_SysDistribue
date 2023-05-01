package ma.miaad.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.miaad.stubs.Bank;
import ma.miaad.stubs.BankServiceGrpc;

import java.io.IOException;

public class BankGrpcClient2 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",8989)
                .usePlaintext()
                .build();
        BankServiceGrpc.BankServiceStub asyncStub=BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request=Bank.ConvertCurrencyRequest.newBuilder()
                .setAmount(4555).
                setCurrencyFrom("DH")
                .setCurrencyTo("EUR")
                .build();
        asyncStub.convert(request, new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println(convertCurrencyResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("FIN...");
            }
        });
        //blocker l'application
        System.out.println("click something...");
        System.in.read();
    }
    }

