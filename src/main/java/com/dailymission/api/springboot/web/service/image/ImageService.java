package com.dailymission.api.springboot.web.service.image;

import com.dailymission.api.springboot.web.repository.common.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final S3Uploader s3Uploader;

    public String genDir(){
        // get calandar instance
        Calendar cal = Calendar.getInstance();

        // get calc Path (year/month/day)
        String yearPath = File.separator + cal.get(Calendar.YEAR);
        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH));
        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

        // return final directory path
        return datePath;
    }

    public String uploadS3(MultipartFile multipartFile, String dirName) throws IOException {
        return s3Uploader.upload(multipartFile, dirName);
    }

}
