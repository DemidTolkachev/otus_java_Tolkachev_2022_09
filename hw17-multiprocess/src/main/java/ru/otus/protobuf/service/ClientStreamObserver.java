package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.OutMessage;

public class ClientStreamObserver implements StreamObserver<OutMessage> {
    private static final Logger logger = LoggerFactory.getLogger(ClientStreamObserver.class);
    private long lastValue = 0;

    @Override
    public void onNext(OutMessage value) {
        lastValue = value.getValue();
        logger.info("new value: {}", lastValue);
    }

    @Override
    public void onError(Throwable t) {
        logger.error(t.getMessage(), t);
    }

    @Override
    public void onCompleted() {
        logger.info("Server stopped.");
    }

    public synchronized long getLastValueAndReset() {
        long result = lastValue;
        lastValue = 0;
        return result;
    }
}