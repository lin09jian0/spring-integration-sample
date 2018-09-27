package com.jim.integration.service;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * @author jim lin
 * 2018/9/21.
 */
@MessagingGateway(defaultRequestChannel = "amqpOutboundChannel")
public interface SendService {

    String sendToRabbit(String data);

}
