package org.example.forum.facades;

import org.example.forum.dto.UtilisateurDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqReceiver implements RabbitListenerConfigurer {

    private final FacadeApplication facadeApplication;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    public RabbitMqReceiver(FacadeApplication facadeApplication) {
        this.facadeApplication = facadeApplication;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void receivedMessage(UtilisateurDTO dto, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {

        if (routingKey.equals("user.subscribe.routingkey")) {
            logger.info("Received user subscribe.");
            logger.info("User { email : " + dto.email() + ", id " + dto.id() + "}");
            facadeApplication.addUser(dto.id(),dto.email());
        } else if (routingKey.equals("user.unsubscribe.routingkey")) {
            logger.info("Received user unsubscribe.");
            logger.info("User { email : " + dto.email() + ", id " + dto.id() + "}");
            facadeApplication.deleteUser(dto.id());
        }
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {

    }

}
