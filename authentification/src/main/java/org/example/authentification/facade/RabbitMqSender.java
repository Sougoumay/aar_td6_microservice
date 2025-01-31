package org.example.authentification.facade;

import org.example.authentification.controleur.dtos.RegisteredDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.subscribe_routingkey}")
    private String subscribe_routingkey;

    @Value("${spring.rabbitmq.unsubscribe_routingkey}")
    private String unsubscribe_routingkey;

    public void send(RegisteredDTO dto, boolean subscribe){
        System.out.println(subscribe);
        if(subscribe){
            rabbitTemplate.convertAndSend(exchange,subscribe_routingkey, dto);
        } else {
            System.out.println("We are in unsubscribe " + dto.email());
            rabbitTemplate.convertAndSend(exchange, unsubscribe_routingkey, dto);
        }
    }
}

