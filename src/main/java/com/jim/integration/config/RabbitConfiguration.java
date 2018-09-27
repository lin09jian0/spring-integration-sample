package com.jim.integration.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundGateway;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;


/**
 * @author jim lin
 *         2018/8/12.
 */
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class RabbitConfiguration {
    @Autowired
    private RabbitProperties rabbitProperties;

    @Bean("connectionFactory")
    public ConnectionFactory getConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setHost(rabbitProperties.getHost());
        cachingConnectionFactory.setPort(rabbitProperties.getPort());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        cachingConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
        //开启发送确认
        cachingConnectionFactory.setPublisherConfirms(true);
        //开启返回确认
        cachingConnectionFactory.setPublisherReturns(true);
        return cachingConnectionFactory;
    }

    @Bean(name="rabbitAdmin")
    public RabbitAdmin getRabbitAdmin()
    {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(getConnectionFactory());
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean("amqpInputChannel")
    public MessageChannel amqpInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public AmqpInboundGateway inbound(SimpleMessageListenerContainer listenerContainer,
                                      @Qualifier("amqpInputChannel") MessageChannel channel) {
        AmqpInboundGateway gateway = new AmqpInboundGateway(listenerContainer);
        gateway.setRequestChannel(channel);
        return gateway;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("queue_direct_A");
        container.setConcurrentConsumers(2);
        // ...
        return container;
    }

    @Bean
    public AmqpTemplate amqpTemplate(){
        AmqpTemplate amqpTemplate = new RabbitTemplate(getConnectionFactory());
        return amqpTemplate;
    }

    @Bean
    @ServiceActivator(inputChannel = "amqpOutboundChannel")
    public AmqpOutboundEndpoint amqpOutbound() {
        AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate());
        outbound.setExpectReply(true);
        outbound.setRoutingKey("direct.routing.keyA");
        outbound.setExchangeName("direct_exchange");
        return outbound;
    }

    @Bean("amqpOutboundChannel")
    public MessageChannel amqpOutboundChannel() {
        return new DirectChannel();
    }


}
