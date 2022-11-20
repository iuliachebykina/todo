package ru.skblab.testtask.service.stub;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.aop.annotation.Loggable;
import ru.skblab.testtask.dto.EmailAddress;
import ru.skblab.testtask.dto.EmailContent;
import ru.skblab.testtask.service.SendMailer;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class SendMailerStub implements SendMailer {

    @Override
    @Loggable
    public void sendMail(EmailAddress toAddress, EmailContent messageBody) throws TimeoutException {
        if(shouldUnsuccessful()) {
            sleep();
            throw new TimeoutException("Timeout!");
        }

        if(shouldSleep()) {
            sleep();
        }

        log.info("\n====================\n" +
                "Email\n" +
                "to: {}\n" +
                "cc: {}\n" +
                "bcc: {}\n" +
                "subject: {}\n" +
                "message: {}\n" +
                "====================\n",
                toAddress.getTo(), toAddress.getCc(), toAddress.getBcc(), messageBody.getSubject(), messageBody.getMessage());

    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }

    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldUnsuccessful() {
        return new Random().nextInt(10) == 1;
    }
}