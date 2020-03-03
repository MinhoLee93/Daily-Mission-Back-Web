package com.dailymission.api.springboot.web.service.image;

import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import com.dailymission.api.springboot.web.repository.common.S3Util;
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

    public String genDir(){
        // get calendar instance
        Calendar cal = Calendar.getInstance();

        // get calc Path (year/month/day)
        String yearPath = "/" + cal.get(Calendar.YEAR);
        String monthPath = yearPath + "/" + new DecimalFormat("00").format(cal.get(Calendar.MONTH));
        String datePath = monthPath + "/" + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        // return final directory path
        return datePath;
    }

    /**
     * /1일1알고리즘/2020/03/28/
     * */
    public MessageDto uploadPostS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName + genDir());
    }

    /**
     * /1일1알고리즘/
     * */
    public MessageDto uploadMissionS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Util.upload(multipartFile, dirName);
    }
}
