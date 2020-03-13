package com.dailymission.api.springboot.web.controller.index;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@Controller
public class IndexController {

    /**
     * Redirect to my Github
     * */
    @GetMapping("/")
    public ResponseEntity<Object> exRedirect5() throws URISyntaxException {
        URI redirectUri = new URI("https://github.com/MinhoLee93");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
