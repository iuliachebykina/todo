package ru.skblab.testtask.service;

import ru.skblab.testtask.dto.Message;
import ru.skblab.testtask.dto.MessageId;

import java.util.concurrent.TimeoutException;

public interface MessagingService {

    /**
     * Отправка сообщения в шину.
     *
     * @param msg сообщение для отправки.
     *
     * @return идентификатор отправленного сообщения (correlationId)
     */
    <Request extends Message, Id> MessageId<Id> send(Request msg);

    /**
     * Встает на ожидание ответа по сообщению с messageId.
     *
     * Редко, но может кинуть исключение по таймауту.
     *
     * @param messageId идентификатор сообщения, на которое ждем ответ.
     * @return Тело ответа.
     */
    < Answer extends Message, Id> Answer receive(MessageId<Id> messageId) throws TimeoutException;

    /**
     * Отправляем сообщение и ждем на него ответ.
     *
     * @param request тело запроса.
     * @return тело ответа.
     */
    <Request extends Message, Answer extends Message> Answer doRequest(Request request) throws TimeoutException;
}
