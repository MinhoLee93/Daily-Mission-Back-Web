package com.dailymission.api.springboot.web.service.rabbitmq;

import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageProducer {
    @Autowired
    RabbitTemplate rabbitTemplate = new RabbitTemplate();

    // mission image resize
    public void sendMessage( Mission mission, MessageDto message){

        message.setMissionId(mission.getId());
        message.setType("mission");
        message.setExtension(mission.getFileExtension());
        message.setOriginalFileName(mission.getOriginalFileName());

        sendMessage(message, "mission");
    }

    // post image resize
    public void sendMessage(User user, Mission mission, Post post, MessageDto message){

        message.setUserId(user.getId());
        message.setMissionId(mission.getId());
        message.setPostId(post.getId());
        message.setType("post");
        message.setExtension(post.getFileExtension());
        message.setOriginalFileName(post.getOriginalFileName());

        sendMessage(message, "post");
    }

    private void sendMessage(MessageDto messageDto, String routingKey){
        rabbitTemplate.convertAndSend("x.resize", routingKey, messageDto);
    }
}
