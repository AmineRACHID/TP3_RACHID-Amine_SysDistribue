package server;

import java.io.IOException;
import java.util.Random;

import game.GameGrpc;
import game.GameOuterClass;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import game.GameOuterClass.GuessRequest;
import game.GameOuterClass.GuessResponse;
import game.GameOuterClass.WinnerRequest;
import game.GameOuterClass.WinnerResponse;

public class GameServer {
    private int secretNumber = new Random().nextInt(1000) + 1;
    private String winner = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        GameServer server = new GameServer();
        Server grpcServer = ServerBuilder.forPort(50051).addService((BindableService) new GameServiceImpl(server)).build();
        grpcServer.start();
        System.out.println("Server started on port " + grpcServer.getPort());
        grpcServer.awaitTermination();
    }

    static class GameServiceImpl extends GameGrpc.GameImplBase {
        private final GameServer server;

        public GameServiceImpl(GameServer server) {
            this.server = server;
        }

        @Override
        public void guess(GuessRequest request, StreamObserver<GuessResponse> responseObserver) {
            if (request.getGuess() == server.secretNumber) {
                server.winner = request.getPlayerName();
                responseObserver.onNext(GuessResponse.newBuilder().setMessage("BRAVO vous avez gagné!").build());
            } else if (request.getGuess() < server.secretNumber) {
                responseObserver.onNext(GuessResponse.newBuilder().setMessage("Votre nombre est plus petit").build());
            } else {
                responseObserver.onNext(GuessResponse.newBuilder().setMessage("Votre nombre est plus grand").build());
            }
            responseObserver.onCompleted();
        }

        @Override
        public void getWinner(WinnerRequest request, StreamObserver<WinnerResponse> responseObserver) {
            if (server.winner != null) {
                responseObserver.onNext(WinnerResponse.newBuilder().setWinner(server.winner).build());
            } else {
                responseObserver.onNext(WinnerResponse.newBuilder().setWinner("Jeu terminé, le gagnant est inconnu").build());
            }
            responseObserver.onCompleted();
        }
    }
}