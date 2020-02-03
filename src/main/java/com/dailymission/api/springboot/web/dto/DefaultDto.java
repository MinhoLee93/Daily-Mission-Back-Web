package com.dailymission.api.springboot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class DefaultDto implements Serializable {

    private final String name;
    private final int amount;

}
