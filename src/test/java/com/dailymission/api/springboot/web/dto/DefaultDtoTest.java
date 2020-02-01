package com.dailymission.api.springboot.web.dto;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


public class DefaultDtoTest {

    @Test
    public void test(){
        // given
        String name = "test";
        int amount = 1;

        // when
        DefaultDto dto = new DefaultDto(name, amount);

        // then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
    }

}
