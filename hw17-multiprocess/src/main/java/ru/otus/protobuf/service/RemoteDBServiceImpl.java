package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.InMessage;
import ru.otus.protobuf.generated.OutMessage;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(RemoteDBServiceImpl.class);

    @Override
    public void getValue(InMessage request, StreamObserver<OutMessage> responseObserver) {
        long firstValue = request.getFirstValue();
        long lastValue = request.getLastValue();
        logger.info("Range: [{}, {}]", firstValue, lastValue);

        for (long i = firstValue + 1; i <= lastValue; i++) {
            responseObserver.onNext(getOutMessage(i));
            logger.info("NextValue: {}", i);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        responseObserver.onCompleted();
    }

    private OutMessage getOutMessage(long value) {
        return OutMessage.newBuilder().setValue(value).build();
    }
}
