package com.dailymission.api.springboot.web.repository.mission;

import com.dailymission.api.springboot.web.dto.participant.ParticipantUserDto;
import com.dailymission.api.springboot.web.dto.post.DateDto;
import com.dailymission.api.springboot.web.dto.post.PostHistoryDto;
import com.dailymission.api.springboot.web.dto.post.PostSubmitDto;
import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.rule.MissionRule;
import com.dailymission.api.springboot.web.repository.mission.rule.Week;
import com.dailymission.api.springboot.web.repository.participant.Participant;
import com.dailymission.api.springboot.web.repository.post.Post;
import com.dailymission.api.springboot.web.repository.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Getter
@NoArgsConstructor
@Entity
public class Mission extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "MISSION_RULE_ID")
    @JsonManagedReference
    private MissionRule missionRule;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "mission")
    @JsonBackReference
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "mission")
    private List<Post> posts = new ArrayList<>();

    @Column(name = "TITLE", nullable = false, length = 20)
    @Size(min = 1, max = 20)
    private String title;

    @Column(name = "CONTENT", nullable = false, length = 50)
    @Size(min = 10, max = 50)
    private String content;

    @Column(name = "ORIGINAL_FILE_NAME", nullable = false)
    private String originalFileName;

    @Column(name = "FILE_EXTENSION", nullable = false)
    private String fileExtension;

    @Column(name="IMAGE_URL", nullable = false)
    private String imageUrl;

    // 썸네일 (Hot)
    @Column(name="THUMBNAIL_URL_HOT")
    private String thumbnailUrlHot;

    // 썸네일 (New)
    @Column(name="THUMBNAIL_URL_NEW")
    private String thumbnailUrlNew;

    // 썸네일 (전체)
    @Column(name="THUMBNAIL_URL_ALL")
    private String thumbnailUrlAll;

    // 썸네일 (디테일)
    @Column(name="THUMBNAIL_URL_DETAIL")
    private String thumbnailUrlDetail;

    @Column(name = "CREDENTIAL", nullable = false)
    private String credential;

    @Column(name = "START_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Column(name = "ENDED", nullable = false)
    private boolean ended;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Builder
    public Mission(MissionRule missionRule, User user, String title, String content,
                   String originalFileName, String fileExtension, String imageUrl, LocalDate startDate, LocalDate endDate, PasswordEncoder passwordEncoder){
        this.missionRule = missionRule;
        this.user = user;
        this.title = title;
        this.content = content;
        this.originalFileName = originalFileName;
        this.fileExtension = fileExtension;
        this.startDate = startDate;
        this.endDate = endDate;

        // s3
        this.imageUrl = imageUrl;
        this.thumbnailUrlNew = imageUrl;
        this.thumbnailUrlAll = imageUrl;
        this.thumbnailUrlHot = imageUrl;
        this.thumbnailUrlDetail = imageUrl;

        this.ended = false;
        this.deleted = false;
    }

    // 비밀번호 생성
    public String setCredential(PasswordEncoder passwordEncoder){

        // generate UUID
        String genUUID = UUID.randomUUID().toString();
        genUUID = genUUID.replace("-","");

        // encoded credential
        this.credential = passwordEncoder.encode(genUUID);

        // return not encoded credential
        return genUUID;
    }

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 비밀번호 확인시에는 password encoder 를 사용해 decode 한다.
     * */
    // 비밀번호 확인
    public boolean checkCredential(String credential, PasswordEncoder passwordEncoder){

        /**
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * 오픈전에 반드시 password encoder 로 변경한다.
         * */
        if(passwordEncoder.matches(credential, this.credential)){
            return true;
        }else{
            return false;
        }
    }

    // 종료 및 삭제여부 확인 (Participant)
    public boolean checkStatus(){
        if(this.ended || this.deleted){
            return false;
        }else{
            return true;
        }
    }

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : 미션 시작날짜 이후에는 참여할 수 없다.
     * */
    // 시작날짜 확인 (Participant Create)
    public boolean checkStartDate(LocalDate date){
        if(date.isAfter(this.startDate)){
            return false;
        }else{
            return true;
        }
    }


    // 종료날짜 확인 (Mission Create)
    public boolean checkEndDate(LocalDate date){
        if(date.isAfter(this.endDate)){
            return false;
        }else{
            return true;
        }
    }

    // 이미지 변경
    public void updateImage(String imageUrl){
        // 이미지
        this.imageUrl = imageUrl;
        // 썸네일 -> 재생성
        this.thumbnailUrlNew = imageUrl;
        this.thumbnailUrlAll = imageUrl;
        this.thumbnailUrlHot = imageUrl;
        this.thumbnailUrlDetail = imageUrl;
    }

    // 썸네일 업데이트 (Hot)
    public void updateThumbnailHot(String thumbnailUrlHot){
        this.thumbnailUrlHot = thumbnailUrlHot;
    }

    // 썸네일 업데이트 (New)
    public void updateThumbnailNew(String thumbnailUrlNew){
        this.thumbnailUrlNew = thumbnailUrlNew;
    }

    // 썸네일 업데이트 (ALL)
    public void updateThumbnailAll(String thumbnailUrlAll){
        this.thumbnailUrlAll = thumbnailUrlAll;
    }

    // 썸네일 업데이트 (Detail)
    public void updateThumbnailDetail(String thumbnailUrlDetail){
        this.thumbnailUrlDetail = thumbnailUrlDetail;
    }

    // 업데이트
    public void update(MissionRule missionRule, String title, String content, LocalDate startDate, LocalDate endDate) {
        this.missionRule = missionRule;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션 delete 하면 자동으로 end
     * */
    public void delete(User user){

        this.deleted = true;
        this.ended = true;

    }

    // 종료
    public void end(){
        this.ended = true;
    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션 참여중인 유저목록 List
     *        아이디 / 이름 / 썸네일 / 강퇴여부
     * */
    public List<ParticipantUserDto> getAllParticipantUser(){
        List<ParticipantUserDto> participantUserList = new ArrayList<>();

        for(Participant p : this.participants){
            User user = p.getUser();

            ParticipantUserDto participantUser = ParticipantUserDto.builder()
                                                                    .id(user.getId())
                                                                    .userName(user.getName())
                                                                    .thumbnailUrl(user.getThumbnailUrl())
                                                                    .banned(p.isBanned())
                                                                    .build();

            participantUserList.add(participantUser);
        }

        return participantUserList;
    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 미션에 참여중인 유저중에 ban 되지 않은 사용자들의 count
     * */
    public int getParticipantCountNotBanned(){

            // count
            int count = 0;

            // check is not banned
            for(Participant p : this.participants){
                if(!p.isBanned()){
                    count++;
                }
            }

            // return count
            return count;
    }


    // 미션 삭제 가능 확인
    public boolean isDeletable(User user){

        // check user
        if(this.user.getId() != user.getId()){
            throw new IllegalArgumentException("허용되지 않은 유저입니다.");
        }

        // check participant is exists
        if(this.participants. size()>1){
            throw new IllegalArgumentException("사용자가 참여중인 미션은 삭제할 수 없습니다.");
        }

        // check is already deleted
        if(this.deleted){
            throw new IllegalArgumentException("이미 삭제된 미션입니다.");
        }

        return true;
    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 매일 새벽 3시에 미션 종료 batch 가 수행된다.
     *        endDate 가 지난 미션을 종료한다.
     * */
    // 미션 종료 가능 확인
    public boolean isEndable(){

        // now
        LocalDate now = LocalDate.now();

        /**
         * [ 2020-03-12 : 이민호 ]
         * 설명 : 종료날짜가 현재보다 이후인 경우에는 종료할 수 없다.
         *       만약 종료날짜가 되지 않았는데, 이 메서드를 호출한 경우는 종료 batch 로직에 문제가 있다.
         * */
        if(this.endDate.isAfter(now)){
            throw new IllegalArgumentException("아직 종료날짜가 되지 않았습니다.");
        }

        return true;
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : week 주의 일주일간 날짜 + 제출의무 요일인지를 반환한다. (일 ~ 월)
     *      ex) 메서드 호출 일자 : 2020-03-13  / week : 0
     *          -> 2020-03-08 ~ 2020-03-14
     *          -> true/true/true/true/true/false/false
     *  */
    public List<DateDto> getWeekDates(LocalDate startDate){

        // result list
        List<DateDto> weekDates = new ArrayList<>();

        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : 일 ~ 월 요일의 날짜  + 제출 의무 요일인지 확인한다.
         *       ## 입력값으로 받은 now 는 항상 일요일이다.
         *  */
        for(int i=0; i<7; i++){
            LocalDate date = startDate.plusDays(i);
            String day = date.getDayOfWeek().toString();
            boolean mandatory = checkMandatory(day);

            DateDto dateDto = DateDto.builder()
                    .date(date)
                    .day(day)
                    .mandatory(mandatory)
                    .build();

            weekDates.add(dateDto);
        }

        return weekDates;
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : String 값인 요일(SUM/MON/TUE...)이 input 으로 주어졌을 때
     *        해당 요일이 제출 의무 요일인지 확인
     *  */
    private boolean checkMandatory(String day){

        Week week = this.missionRule.getWeek();

        if(day.equals("SUNDAY")){
            return week.isSun();
        }else if(day.equals("MONDAY")){
            return week.isMon();
        }else if(day.equals("TUESDAY")){
            return week.isTue();
        }else if(day.equals("WEDNESDAY")){
            return week.isWed();
        }else if(day.equals("THURSDAY")){
            return week.isThu();
        }else if(day.equals("FRIDAY")){
            return week.isFri();
        }else if(day.equals("SATURDAY")){
            return week.isSat();
        }

        return false;
    }

    /**
     * [ 2020-03-13 : 이민호 ]
     * 설명 : 전달받은 PostSubmitDto 객체를 조합해 유저별로 묶는다.
     *        결과값 : 유저정보 + 제출일자 리스트 {'2020-03-08', '2020-03-14'...}
     * */
    public List<PostHistoryDto> getHistories(List<PostSubmitDto> submits){

        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : 유저별 PostSubmitDto 를 담을 hashMap
         *        key : user id
         * */
        // key : user id / value : post history
        Map<Long, List<PostSubmitDto>> allUserSubmits = new HashMap<>();


        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : 유저별로 구분해서 PostHistory 를 hash map 에 담는다.
         * */
        for(PostSubmitDto dto : submits){
            if(allUserSubmits.containsKey(dto.getUserId())){
                allUserSubmits.get(dto.getUserId()).add(dto);
            }else{
                List<PostSubmitDto> temp = new ArrayList<>();
                temp.add(dto);
                allUserSubmits.put(dto.getUserId(), temp);
            }
        }

        // result
        List<PostHistoryDto> histories = new ArrayList<>();

        /**
         * [ 2020-03-13 : 이민호 ]
         * 설명 : 미션에 참여중인 참여자들 별로
         *        hash 에 담아놓은 PostSubmitDto 를 확인해
         *        PostHistoryDto 로 묶는다.
         *
         *        즉 참여자별로 제출 기록을 하나로 묶어 historyDto 객체로 변환한다.
         * */
        for(Participant participant : this.participants){

            // user
            User user = participant.getUser();


            /**
             * [ 2020-03-13 : 이민호 ]
             * 설명 : history 에 USER 의 기본정보를 담는다.
             *       이때 미션에서 강퇴되었는지 여부도 포함한다. (isBanned)
             * */
            PostHistoryDto history = PostHistoryDto.builder()
                    .userId(user.getId())
                    .userName(user.getName())
                    .thumbnailUrl(user.getThumbnailUrl())
                    .banned(participant.isBanned())
                    .build();


            /**
             * [ 2020-03-13 : 이민호 ]
             * 설명 : hash 에서 user id key 해당하는 postHistoryDto list 를 가져온다.
             * */
            List<PostSubmitDto> userSubmits = allUserSubmits.get(user.getId());
            // check null
            if(userSubmits != null){
                Iterator<PostSubmitDto> iterator = userSubmits.iterator();

                /**
                 * [ 2020-03-13 : 이민호 ]
                 * 설명 : 유저의 제출 기록이 있는 경우 해당 기록들을 history 의 date array 에 담는다.
                 * */
                while(iterator.hasNext()){

                    /**
                     * [ 2020-03-13 : 이민호 ]
                     * 설명 : 만약 중복된 date 가 담겼을 경우에는 포스트 제출 로직에 문제가 있다.
                     *        동일 제출일자에 중복해서 동일 미션에 포스트를 제출할 수 없게 설계되어 있다.
                     * */
                    PostSubmitDto submit = iterator.next();
                    history.getDate().add(submit.getDate());
                }

            }

            /**
             * [ 2020-03-13 : 이민호 ]
             * 설명 : history 는 참여자 한명의 weekly submit history 를 담고 있다.
             *       전체 참여자의 weekly submit history 를 담고 있는 histories 에 이를 add 한다.
             * */
            histories.add(history);
        }

        // return histories
        return histories;
    }
}
