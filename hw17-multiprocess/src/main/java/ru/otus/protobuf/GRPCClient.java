package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.InMessage;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.protobuf.service.ClientStreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT).usePlaintext().build();
        long currentValue = 0;
        var latch = new CountDownLatch(1);
        var newStub = RemoteDBServiceGrpc.newStub(channel);
        InMessage inMessage = InMessage.newBuilder().setFirstValue(0).setLastValue(30).build();
        logger.info("Client is starting...");
        ClientStreamObserver clientStreamObserver = new ClientStreamObserver();
        newStub.getValue(inMessage, clientStreamObserver);

        for (int i = 0; i < 50; i++) {
            currentValue = currentValue + clientStreamObserver.getLastValueAndReset() + 1;
            logger.info("currentValue: {}", currentValue);
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        latch.await();
        channel.shutdown();
    }
}