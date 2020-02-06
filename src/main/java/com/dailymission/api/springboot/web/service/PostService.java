package com.dailymission.api.springboot.web.service;

import com.dailymission.api.springboot.web.domain.post.Post;
import com.dailymission.api.springboot.web.domain.post.PostRepository;
import com.dailymission.api.springboot.web.dto.post.PostListResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.dto.post.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long save(PostSaveRequestDto requestDto){
        return postRepository.save(requestDto.toEntitiy()).getId();
    }

    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        post.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById (Long id){
        Post entity = postRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostResponseDto(entity);
    }

    public List<PostListResponseDto> findAllDesc(){
        return postRepository.findAllDesc().stream()
                                .map(PostListResponseDto::new)
                                .collect(Collectors.toList());

    }

    @Transactional
    public void delete(Long id){
        Post post = postRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        postRepository.delete(post);
    }
}
