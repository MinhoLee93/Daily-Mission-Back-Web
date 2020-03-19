package com.dailymission.api.springboot.web.user;

import com.dailymission.api.springboot.web.repository.user.User;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * [ 2020-03-18 : 이민호 ]
 * - 객체지향에서 본인의 책임(기능)은 본인 스스로가 제공해야 합니다.
 * - 특히 엔티티 객체들은 가장 핵심 객체이고 이 객체를 사용하는 계층들이 다양하게 분포되기 때문에 반드시 테스트 코드를 작성해야합니다.
 * - entity, Embeddable 객체 등의 객체들도 반드시 테스트 코드를 작성해야합니다.
 * */
public class UserTest {

    private final String USER_NAME_SMALLER_THEN_MIN_LENGTH = "";
    private final String USER_NAME_LARGER_THEN_MAX_LENGTH = "USER_NAME_LARGER_THEN_MAX_LENGTH";
    private final String IMAGE_URL = "IMAGE_URL.jpg";
    private final String THUMBNAIL_URL = "THUMBNAIL_URL.jpg";
    private final String USER_NAME = "USER_NAME";
    private final String UPDATE_USER_NAME = "UPDATE_USER_NAME";
    private User user;


    @Before
    public void setup() throws Exception {
        // user
        user = User.builder()
                    .name(USER_NAME)
                    .imageUrl(IMAGE_URL)
                    .build();
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER 생성 후 이름 확인
     * */
    @Test
    public void create_user_THEN_check_user_name(){
        // then
        assertThat(user.getName()).isEqualTo(USER_NAME);
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : NAME 변경시 변경할 name 이 NULL 일 경우
     *        isValidUpdateName 이 false 를 반환하는지 확인
     * */
    @Test
    public void update_username_is_Null_THEN_isValidUpdateName_return_false(){

        // then
        assertThat(user.isValidUpdateName(null)).isFalse();
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : NAME 변경시 변경할 name 이 기존 name 과 동일할 경우
     *        isValidUpdateName 이 false 를 반환하는지 확인
     * */
    @Test
    public void update_username_is_same_with_current_name_THEN_isValidUpdateName_return_false(){

        // then
        assertThat(user.isValidUpdateName(null)).isFalse();
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : NAME 변경시 USER NAME 의 길이가 USER_NAME_MIN_LENGTH 보다 작으면
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void update_username_LENGTH_is_smaller_then_USER_NAME_MIN_LENGTH_THNE_throw_IllegalArgumentException(){

        // when
        user.isValidUpdateName(USER_NAME_SMALLER_THEN_MIN_LENGTH);
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : NAME 변경시 USER NAME 의 길이가 USER_NAME_MAX_LENGTH 보다 크면
     *        IllegalArgumentException 에러가 발생한다.
     * */
    @Test(expected = IllegalArgumentException.class)
    public void update_username_LENGTH_is_larger_then_USER_NAME_MAX_LENGTH_THNE_throw_IllegalArgumentException(){

        // when
        user.isValidUpdateName(USER_NAME_LARGER_THEN_MAX_LENGTH);
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : USER name update 후 update 된 이름 확인
     * */
    @Test
    public void update_username_THEN_check_is_updated(){
        // when
        user.updateName(UPDATE_USER_NAME);

        // then
        assertThat(user.getName()).isEqualTo(UPDATE_USER_NAME);
    }


    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : 이미지 변경시 image 가 변경된다.
     *        Thumbnail 도 동일한 값으로 변경된다.
     * */
    @Test
    public void update_image_url_THEN_check_is_updated(){

        // when
        user.updateImage(IMAGE_URL);

        // then
        assertThat(user.getImageUrl()).isEqualTo(IMAGE_URL);
        assertThat(user.getThumbnailUrl()).isEqualTo(IMAGE_URL);
    }

    /**
     * [ 2020-03-18 : 이민호 ]
     * 설명 : Thumbnail 변경시 Thumbnail 만 변경된다.
     *       Image 는 변경되지 않는다.
     * */
    @Test
    public void update_thumbnail_url_THEN_check_is_updated(){

        // when
        user.updateThumbnail(THUMBNAIL_URL);

        // then
        assertThat(user.getThumbnailUrl()).isEqualTo(THUMBNAIL_URL);
        assertThat(user.getImageUrl()).isNotEqualTo(THUMBNAIL_URL);

    }
}
