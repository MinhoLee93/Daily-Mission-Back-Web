package com.dailymission.api.springboot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class DefaultDto {

    private final String name;
    private final int amount;

}
