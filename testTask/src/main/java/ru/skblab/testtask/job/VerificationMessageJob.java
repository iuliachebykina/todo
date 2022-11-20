package ru.skblab.testtask.job;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.skblab.testtask.service.UnsentVerificationMessageService;
import ru.skblab.testtask.service.VerificationService;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class VerificationMessageJob {

    final VerificationService verificationService;
    final UnsentVerificationMessageService unsentVerificationMessageService;

    // тк в любой момент системы могут дать сбой, я решила отправлять сообщения во внешнюю систему отдельной джобой
    // каждые 5 минут (условно) джоба находит все неотправленные сообщения и пытается отправить
    @Scheduled(cron = "${job.unsent-verification-messages.cron: 0 */5 * * * *}")
    public void sendAllUnsentVerificationMessages() {
        log.info("JOB sent notification messages ...");
        List<Long> userIds = unsentVerificationMessageService.findAllUserIdWithUnsentVerificationMessage();
        userIds.forEach(verificationService::verifyUser);
        log.info("JOB sent notification messages ... DONE");
        log.info("JOB sent notification messages. Total count processing user: {}", userIds.size());
    }
}
