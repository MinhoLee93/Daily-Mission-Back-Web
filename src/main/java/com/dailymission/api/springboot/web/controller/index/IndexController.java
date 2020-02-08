package com.dailymission.api.springboot.web.controller.index;

import com.dailymission.api.springboot.web.dto.post.PostResponseDto;
import com.dailymission.api.springboot.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostService postService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("post", postService.findAllDesc());

//        if(account != null){
//            model.addAttribute("userName", account.getName());
//        }

        return "index";
    }

    @GetMapping("/post/save")
    public String postsSave(){
        return "post-save";
    }

    @GetMapping("/post/update/{id}")
    public String postUpdate(@PathVariable Long id, Model model){
        PostResponseDto dto = postService.findById(id);
        model.addAttribute("post", dto);

        return "post-update";
    }
}
