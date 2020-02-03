package com.dailymission.api.springboot.web.controller;

import com.dailymission.api.springboot.config.auth.SecurityConfig;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DefaultController.class,
        excludeFilters = {@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
public class DefaultControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles = "USER")
    public void test() throws Exception {
        String result = "test";

        mvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }

    @Test
    @WithMockUser(roles = "USER")
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
