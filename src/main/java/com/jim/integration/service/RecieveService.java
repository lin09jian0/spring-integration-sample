package com.jim.integration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

/**
 * @author jim lin
 * 2018/9/20.
 */
@MessageEndpoint
@Slf4j
public class RecieveService {

    @ServiceActivator(inputChannel="amqpInputChannel")
    public void recieve(String message){
        log.info("=============:{}",message);
    }
}
