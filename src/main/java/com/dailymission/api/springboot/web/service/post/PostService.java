package com.dailymission.api.springboot.web.service.post;

import com.dailymission.api.springboot.web.dto.post.PostListResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostResponseDto;
import com.dailymission.api.springboot.web.dto.post.PostSaveRequestDto;
import com.dailymission.api.springboot.web.dto.post.PostUpdateRequestDto;
import com.dailymission.api.springboot.web.repository.account.AccountRepository;
import com.dailymission.api.springboot.web.repository.mission.MissionRepository;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.post.PostRepository;
import com.dailymission.api.springboot.web.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final MissionRepository missionRepository;

    private final AccountRepository accountRepository;

    private final ImageService imageService;

    @Transactional
    public Long save(PostSaveRequestDto requestDto, MultipartFile file) throws IOException {
        Post post = requestDto.toEntitiy();

        if(!missionRepository.existsById(post.getMission().getId())){
            throw  new NoSuchElementException("존재하지 않는 미션입니다. ID=" + post.getMission().getId());
        }

        if(!accountRepository.existsById(post.getAccount().getId())){
            throw  new NoSuchElementException("존재하지 않는 유저입니다. ID=" + post.getAccount().getId());
        }

        post = postRepository.save(post);

        // change image
        if(!file.isEmpty()){
            String imagePath = imageService.uploadS3(file, post.getMission().getTitle() + imageService.genDir());
            post.updateImage(imagePath);
        }

        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById (Long id){
        Optional<Post> optional = Optional.ofNullable(postRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        Post post = optional.get();
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllDesc(){
        return postRepository.findAllDesc().stream()
                .map(PostListResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto){
        Optional<Post> optional = Optional.ofNullable(postRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        Post post = optional.get();
        post.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public Long updateImage(Long id, MultipartFile file) throws IOException {
        Optional<Post> optional = Optional.ofNullable(postRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 없습니다. id=" + id));

        Post post = optional.get();

        // change image
        String imagePath = imageService.uploadS3(file, post.getMission().getTitle() + imageService.genDir());
        post.updateImage(imagePath);

        return id;
    }


    @Transactional
    public void delete(Long id){
        Optional<Post> optional = Optional.ofNullable(postRepository.findById(id))
                        .orElseThrow(()-> new NoSuchElementException("해당 게시글이 없습니다. id =" + id));

        // delete flag -> 'Y'
        Post post = optional.get();
        post.delete();
    }


}
