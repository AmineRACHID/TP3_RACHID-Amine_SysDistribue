package client;

import game.GameOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import game.GameGrpc;
import game.GameOuterClass.GuessRequest;
import game.GameOuterClass.GuessResponse;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;

public class GameClient {
    private final ManagedChannel channel;
    private final GameGrpc.GameStub stub;
    private final Scanner scanner;

    public GameClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = GameGrpc.newStub(channel);
        this.scanner = new Scanner(System.in);
    }

    public void play() {
        System.out.println("Enter your guess:");
        String playerName = scanner.nextLine();

        StreamObserver<GuessResponse> responseObserver = new StreamObserver<GuessResponse>() {
            @Override
            public void onNext(GuessResponse response) {
                System.out.println(response.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Game Over");
            }
        };

        StreamObserver<GuessRequest> requestObserver = stub.guess(responseObserver);

        while (true) {
            int guess = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            requestObserver.onNext(GuessRequest.newBuilder()
                    .setPlayerName(playerName)
                    .setGuess(guess)
                    .build());

            System.out.println("Do you want to continue? (y/n)");
            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("n")) {
                requestObserver.onCompleted();
                break;
            }
        }

        stub.getWinner(null, new StreamObserver<GameOuterClass.WinnerResponse>() {
            @Override
            public void onNext(GameOuterClass.WinnerResponse response) {
                System.out.println("The winner is " + response.getWinner());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });
    }

    public static void main(String[] args) {
        GameClient client = new GameClient("localhost", 50051);
        client.play();
    }
}
