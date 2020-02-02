package com.dailymission.api.springboot.web.controller;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DefaultControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test() throws Exception {
        String result = "test";

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }

    @Test
    public void dto_test() throws Exception {
        // given
        String name = "test";
        int amount = 1;

        // when
        mvc.perform(get("/default/dto")
                    .param("name", name)
                    .param("amount", String.valueOf(amount)))
        // then
                    .andExpect(jsonPath("$.name", Matchers.is(name)))
                    .andExpect(jsonPath("$.amount", Matchers.is(amount)));


    }
}
