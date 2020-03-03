package com.dailymission.api.springboot.web.service.rabbitmq;

import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageProducer {
    @Autowired
    RabbitTemplate rabbitTemplate = new RabbitTemplate();

    public void sendMessage(MessageDto messageDto, String routingKey){
        rabbitTemplate.convertAndSend("x.resize", routingKey, messageDto);
    }
}
