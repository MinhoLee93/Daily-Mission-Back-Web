package com.dailymission.api.springboot.web.controller;

import com.sun.org.apache.regexp.internal.RE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.security.RunAs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class DefualtControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void hello_world_return() throws Exception {
        String result = "Hello World";

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }
}
