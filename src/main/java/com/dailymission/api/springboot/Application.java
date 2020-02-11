package com.dailymission.api.springboot;

import com.dailymission.api.springboot.config.AppProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


/*
* We’ll need to enable configuration properties by adding the @EnableConfigurationProperties annotation.
* Please open the main application class SpringSocialApplication.java and add the annotation like so-
* */
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-oauth.yml,"
            + "classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
