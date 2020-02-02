package com.dailymission.api.springboot.web.dto;


import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
