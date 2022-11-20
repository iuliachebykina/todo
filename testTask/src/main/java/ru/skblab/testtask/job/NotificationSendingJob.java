package ru.skblab.testtask.job;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.skblab.testtask.service.NotificationService;
import ru.skblab.testtask.service.UnsentNotificationService;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class NotificationSendingJob {

    final NotificationService notificationService;
    final UnsentNotificationService unsentNotificationService;


    // тк в любой момент системы могут дать сбой, я решила отправлять уведомления отдельной джобой
    // каждые 5 минут (условно) джоба находит все неотправленные уведомления и пытается отправить

    // так же была идея создать 2 еще очерреди (что-то типо DeadLetterQueue) в которые бы отправлялись сообщения,
    // которые не смогли быть отправлены/прочитаны.

    // Но подумав, я решила остановится на сохранении данных в базе и вычитыванием джобой, тк это проще)))
    // Обработка мертвых писем (из того что я почитала) достаточно затратная вещь + тк нужны еще очередедь и логика их обработки
    //  а информацию о верификации все равно нужно хранить в базе

    // +, наверняка, не исключен такой вариант, когда отказывает сам брокер сообщений, то есть данные не просто не обрабатываются 2 стороной,
    // но даже не отправляются. Например, сам сервер, на котором он находится, дал сбой

    // а для отправки писем пришлось бы организовывать в принципе новую основную очередь, консьюмера и листенера
    // а потом еще и очередь мертвых писем

    // Конечно, может встать вопрос излишнюю нагрузку на базу
    // но я не считаю, что это будет критично, если навесить индексы на вызываемые поля
    @Scheduled(cron = "${job.unsent-notification.cron:  0 */5 * * * *}")
    public void sendAllUnsentNotifications() {
        log.info("JOB sent notifications ...");
        List<Long> userIds = unsentNotificationService.findAllUserIdWithUnsentNotification();
        userIds.forEach(notificationService::notifyUserAboutVerification);
        log.info("JOB sent notifications ... DONE");
        log.info("JOB sent notifications. Total count processing user: {}", userIds.size());

    }
}
