package com.jim.integration;

import com.jim.integration.service.SendService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.config.EnableIntegration;

/**
 * @author jim lin
 * 2018/9/20.
 */
@SpringBootApplication
@EnableIntegration
public class IntegrationBootstrap {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(IntegrationBootstrap.class,args);
        SendService sendService = context.getBean(SendService.class);
        sendService.sendToRabbit("sendMessage===================");
    }
}
