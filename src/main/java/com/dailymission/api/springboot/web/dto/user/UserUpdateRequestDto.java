package com.dailymission.api.springboot.web.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private Long id;
    private String userName;
    private MultipartFile file;

    @Builder
    public UserUpdateRequestDto(Long id, String userName, MultipartFile file){
        this.id = id;
        this.userName = userName;
        this.file = file;
    }
}

