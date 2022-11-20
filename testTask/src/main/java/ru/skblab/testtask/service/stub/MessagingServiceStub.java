package ru.skblab.testtask.service.stub;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.aop.annotation.Loggable;
import ru.skblab.testtask.dto.Message;
import ru.skblab.testtask.dto.MessageId;
import ru.skblab.testtask.dto.UserVerifiedAnswerMessage;
import ru.skblab.testtask.service.MessagingService;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class MessagingServiceStub implements MessagingService {
    @Override
    @Loggable
    public <Request extends Message, Id> MessageId<Id> send(Request msg) {
        MessageId<Id> id = new MessageId<>((Id) UUID.randomUUID());
        log.info("\n====================\n" +
                "Message send:\n" +
                "id: {}\n" +
                "body: {}\n" +
                "====================\n", id, msg);
        return id;

    }

    @Override
    @Loggable
    public <Answer extends Message, Id> Answer receive(MessageId<Id> messageId) throws TimeoutException {

        if (shouldUnsuccessful()) {
            sleep();

            throw new TimeoutException("Timeout!");
        }

        if (shouldSleep()) {
            sleep();
        }

        UserVerifiedAnswerMessage answer = UserVerifiedAnswerMessage.builder()
                .isVerified(new Random().nextBoolean())
                .build();

        log.info("\n====================\n" +
                "Message received:\n" +
                "id: {}\n" +
                "body: {}\n" +
                "====================\n", messageId, answer);
        return (Answer) answer;
    }


    @Override
    @Loggable
    public <Request extends Message, Answer extends Message> Answer doRequest(Request request) throws TimeoutException {
        final MessageId<UUID> messageId = send(request);

        return receive(messageId);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
    }


    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldUnsuccessful() {
        return new Random().nextInt(10) == 1;
    }
}
