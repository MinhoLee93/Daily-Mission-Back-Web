package com.dailymission.api.springboot.web.controller.index;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    /**
     * Redirect to my Github
     * */
    @GetMapping("/")
    public String index() {
//        URI redirectUri = new URI("https://github.com/MinhoLee93");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(redirectUri);
//        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

        return "index";
    }
}
