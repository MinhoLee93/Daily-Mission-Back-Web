package com.dailymission.api.springboot.web.dto.rabbitmq;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private Long userId;
    private Long missionId;
    private Long postId;
    private String type;
    private String dirName;
    private String fileName;
    private String extension;
    private String keyName;
    private String originalFileName;
    private String imageUrl;

    @Builder
    public MessageDto(Long userId, Long missionId, long postId, String type, String dirName, String fileName, String extension, String keyName, String originalFileName, String imageUrl){
        this.userId = userId;
        this.missionId = missionId;
        this.postId = postId;
        this.type =type;
        this.dirName = dirName;
        this.fileName = fileName;
        this.extension = extension;
        this.keyName = keyName;
        this.originalFileName = originalFileName;
        this.imageUrl = imageUrl;
    }
}
