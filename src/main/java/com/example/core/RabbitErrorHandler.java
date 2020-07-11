package com.example.core;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component
public class RabbitErrorHandler implements RabbitListenerErrorHandler {

    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
                              ListenerExecutionFailedException exception) {
        throw new AmqpRejectAndDontRequeueException("An error occured");
    }
}
