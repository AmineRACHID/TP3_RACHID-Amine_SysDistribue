package ma.miaad.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.miaad.stubs.Bank;
import ma.miaad.stubs.BankServiceGrpc;

import java.io.IOException;

public class BankGrpcClient3 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",8989)
                .usePlaintext()
                .build();

        BankServiceGrpc.BankServiceStub asyncStub=BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request=Bank.ConvertCurrencyRequest.newBuilder()
                .setAmount(7888).
                setCurrencyFrom("DH")
                .setCurrencyTo("EUR")
                .build();
       asyncStub.getCurrencyStream(request, new StreamObserver<Bank.ConvertCurrencyResponse>() {
           @Override
           public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
               System.out.println("***************************");
               System.out.println(convertCurrencyResponse);
               System.out.println("***************************");
           }

           @Override
           public void onError(Throwable throwable) {
               System.out.println(throwable.getMessage());

           }

           @Override
           public void onCompleted() {
               System.out.println("FIIIIIIIIIN...");

           }
       });
        //blocker l'application
        System.out.println("click something...");
        System.in.read();
    }
    }

