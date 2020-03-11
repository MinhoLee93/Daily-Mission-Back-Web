package com.dailymission.api.springboot.web.service.image;

import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.repository.common.S3Util;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final S3Util s3Util;

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 :  POST directory 생성
     *
     * return : /2020/03/11
     * */
    public String getPostDir(){
        // get calendar instance
        Calendar cal = Calendar.getInstance();

        // get calc Path (year/month/day)
        String yearPath = "/" + cal.get(Calendar.YEAR);
        String monthPath = yearPath + "/" + new DecimalFormat("00").format(cal.get(Calendar.MONTH)+1);
        String datePath = monthPath + "/" + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        // return final directory path
        return datePath;
    }


    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 :  USER directory 생성
     *
     * return : /google/123
     * */
    public String getUserDir(User user){
        String dirName = "" + user.getProvider().toString() + "/" + user.getId();

        return dirName;
    }


    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 :  POST 이미지 업로드
     *
     * directory : /mission.title/year/month/day/
     *         ex) /1일1알고리즘/2020/03/28/
     * */
    public MessageDto uploadPostS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName + getPostDir());
    }

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : MISSION 이미지 업로드
     *
     * directory : /mission.title/
     *         ex) /1일1알고리즘/
     * */
    public MessageDto uploadMissionS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName);
    }

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : USER 이미지 업로드
     *
     * directory : /provider/id/
     *         ex) /google/123/
     * */
    public MessageDto uploadUserS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName);
    }
}
