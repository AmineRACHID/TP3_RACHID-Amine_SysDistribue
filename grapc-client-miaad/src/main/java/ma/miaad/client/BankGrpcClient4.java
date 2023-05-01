 package ma.miaad.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.miaad.stubs.Bank;
import ma.miaad.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

 public class BankGrpcClient4 {
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
        StreamObserver<Bank.ConvertCurrencyRequest> performStream = asyncStub.performStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("++++++++++++++++++");
                System.out.println(convertCurrencyResponse);
                System.out.println("++++++++++++++++++");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("FIN");
            }
        });
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            int counter=0;
            @Override
            public void run() {
                Bank.ConvertCurrencyRequest currencyRequest= Bank.ConvertCurrencyRequest.newBuilder()
                                .setAmount(Math.random()*7800)
                                        .build();
                performStream.onNext(currencyRequest);
                System.out.println("++++++++++++++++++++++counter="+counter);
                ++counter;
                if(counter==20){
                    performStream.onCompleted();
                    timer.cancel();
                }
            }
        }, 1000, 1000);
        //blocker l'application
        System.out.println("click something...");
        System.in.read();
    }
    }

