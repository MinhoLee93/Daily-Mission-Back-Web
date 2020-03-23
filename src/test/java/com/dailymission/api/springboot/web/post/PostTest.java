package com.dailymission.api.springboot.web.post;

import com.dailymission.api.springboot.web.mission.MissionSetup;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import com.dailymission.api.springboot.web.user.UserSetup;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * [ 2020-03-23 : 이민호 ]
 * - 객체지향에서 본인의 책임(기능)은 본인 스스로가 제공해야 합니다.
 * - 특히 엔티티 객체들은 가장 핵심 객체이고 이 객체를 사용하는 계층들이 다양하게 분포되기 때문에 반드시 테스트 코드를 작성해야합니다.
 * - entity, Embeddable 객체 등의 객체들도 반드시 테스트 코드를 작성해야합니다.
 * */
public class PostTest {

    private final String TITLE = "TITLE";
    private final String UPDATE_TITLE = "UPDATE_TITLE";
    private final String CONTENT = "CONTENT";
    private final String UPDATE_CONTENT = "UPDATE_CONTENT";
    private final String ORIGINAL_FILE_NAME = "ORIGINAL_FILE_NAME.jpg";
    private final String FILE_EXTENSION = ".jpg";
    private final String IMAGE_URL = "IMAGE_URL.jpg";
    private final String UPDATE_IMAGE_URL = "UPDATE_IMAGE_URL.jpg";
    private final String THUMBNAIL_URL ="THUMBNAIL_URL.jpg";

    private Mission mission;
    private User user;
    private Post post;



    @Before
    public void setup(){
        // user
        user = UserSetup.builder().build().get();

        // mission
        mission = MissionSetup.builder().user(user).build().get();

        // post
        post = Post.builder()
                   .user(user)
                   .mission(mission)
                   .title(TITLE)
                   .content(CONTENT)
                   .originalFileName(ORIGINAL_FILE_NAME)
                   .fileExtension(FILE_EXTENSION)
                   .imageUrl(IMAGE_URL)
                   .build();
    }

    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : POST 생성후 변수 확인
     *       THUMBNAIL_URL 은 IMAGE_URL 과 동일하며
     *       DELETE 는 False 이다.
     * */
    @Test
    public void create_post_THEN_check_post_variables(){

        // then
        assertThat(post.getImageUrl()).isEqualTo(IMAGE_URL);
        /**
         * [ 2020-03-20 : 이민호 ]
         * 설명 : POST 생성시 Thumbnail url 은 리사이징 전까지 Image url 과 동일하다.
         * */
        assertThat(post.getThumbnailUrl()).isEqualTo(IMAGE_URL);
        assertThat(post.isDeleted()).isFalse();
    }

//    /**
//     * [ 2020-03-23 : 이민호 ]
//     * 설명 : TITLE/CONTENT 업데이트 후 업데이트 되었는지 확인
//     * */
//    @Test
//    public void update_post_THEN_check_post_is_updated(){
//        // when
//        post.update(UPDATE_TITLE, UPDATE_CONTENT);
//
//        // then
//        assertThat(post.getTitle()).isEqualTo(UPDATE_TITLE);
//        assertThat(post.getContent()).isEqualTo(UPDATE_CONTENT);
//    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : IMAGE UPDATE 후 IMAGE_URL/THUMBNAIL_URL 확인
     * */
    @Test
    public void update_post_image_THEN_check_image_and_thumbnail_is_updated(){
        // when
        post.updateImage(UPDATE_IMAGE_URL);

        // then
        assertThat(post.getImageUrl()).isEqualTo(UPDATE_IMAGE_URL);
        assertThat(post.getThumbnailUrl()).isEqualTo(UPDATE_IMAGE_URL);
    }

    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : THUMBNAIL 업데이트 후 THUMBNAIL 확인
     *        IMAGE 는 UPDATE 되지 않았는지 확인
     * */
    @Test
    public void update_post_thumbnail_THEN_check_thumbnail_is_updated(){
        // when
        post.updateThumbnail(THUMBNAIL_URL);

        // then
        assertThat(post.getThumbnailUrl()).isEqualTo(THUMBNAIL_URL);
        assertThat(post.getImageUrl()).isEqualTo(IMAGE_URL);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : POST 생성자가 아닌 USER 가 isDeletable 을 호출할 경우
     *        IllegalArgumentException 가 발생하는지
     * */
    @Test(expected = IllegalArgumentException.class)
    public void IllegalUser_call_isDeletable_THEN_throw_IllegalArgumentException(){
        // given
        User IllegalUser = UserSetup.builder().build().get();

        // when
        post.isDeletable(IllegalUser);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 이미 삭제된 POST 를 삭제하려고하는 경우
     *        IllegalArgumentException 가 발생하는지
     * */
    @Test(expected = IllegalArgumentException.class)
    public void already_deleted_post_call_isDeletable_THEN_throw_IllegalArgumentException(){
        // given
        post.delete();

        // when
        post.isDeletable(user);
    }


    /**
     * [ 2020-03-23 : 이민호 ]
     * 설명 : 삭제 후 삭제되었는지 확인
     * */
    @Test
    public void check_delete_success(){
        // given
        post.delete();

        // when
        assertThat(post.isDeleted()).isTrue();
    }
}
