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

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : USER 이미지 Resizing 메세지 생성
     * * */
    // create user image resize message
    public void sendMessage(User user, MessageDto message){

        message.setUserId(user.getId());
        message.setType("user");
        message.setExtension(user.getFileExtension());
        message.setOriginalFileName(user.getOriginalFileName());

        sendMessage(message, "user");
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : MISSION 이미지 Resizing 메세지 생성
     * * */
    // create mission image resize message
    public void sendMessage(Mission mission, MessageDto message){

        message.setMissionId(mission.getId());
        message.setType("mission");
        message.setExtension(mission.getFileExtension());
        message.setOriginalFileName(mission.getOriginalFileName());

        sendMessage(message, "mission");
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : POST 이미지 Resizing 메세지 생성
     * * */
    // create post image resize message
    public void sendMessage(User user, Mission mission, Post post, MessageDto message){

        message.setUserId(user.getId());
        message.setMissionId(mission.getId());
        message.setPostId(post.getId());
        message.setType("post");
        message.setExtension(post.getFileExtension());
        message.setOriginalFileName(post.getOriginalFileName());

        sendMessage(message, "post");
    }


    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 실제로 메시지를 보내는 메서드
     * */
    // send image resize message
    private void sendMessage(MessageDto messageDto, String routingKey){
        rabbitTemplate.convertAndSend("x.resize", routingKey, messageDto);
    }
}
