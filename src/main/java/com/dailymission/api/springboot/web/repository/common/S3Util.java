package com.dailymission.api.springboot.web.repository.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dailymission.api.springboot.web.dto.rabbitmq.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Util {

    // amazon client
    private final AmazonS3 amazonS3Client;

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : aws.yml 에 선언되있는 s3 bucket 정보를 가져온다.
     * */
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 현재 날짜에 해당하는 serial number 를 생성한다.
     *        이미지 저장시 중복되는 이름이 있을경우 덮어씌워지는 것을 방지하기위해
     *        이미지 이름 앞에 붙여서 저장한다.
     *
     * ex) yyyyMMddHHmm : 202003120505
     * */
    public String genSerialNumber(){
        LocalDateTime now = LocalDateTime.now();
        String serial = "" +  new DecimalFormat("0000").format(now.getYear())
                + new DecimalFormat("00").format(now.getMonthValue())
                + new DecimalFormat("00").format(now.getDayOfMonth())
                + new DecimalFormat("00").format(now.getHour())
                + new DecimalFormat("00").format(now.getMinute());

        return serial;
    }

    /**
     * [ 2020-03-11 : 이민호 ]
     * 설명 : MultipartFile 로 전달받은 객체를 File 로 Convert 하고
     *        S3에 업로드 하는 upload 함수에 전달한다.
     *
     *       upload 후에 전달받은 messageDto 에 originFileName 과 fileExtension 변수를 추가해 return 한다.
     * */
    public MessageDto upload(MultipartFile multipartFile, String dirName) throws IOException {

        // convert MultipartFile -> File
        File uploadFile = convert(multipartFile)
            .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        // messageDto
        MessageDto messageDto = upload(uploadFile, dirName);

        /**
         * [ 2020-03-11 : 이민호 ]
         * 설명 : originalFileName + fileExtension 은 DB에 저장하기위한 정보
         * */
        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));


        // set message
        messageDto.setOriginalFileName(multipartFile.getOriginalFilename());
        messageDto.setExtension(fileExtension);

        return messageDto;
    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : File 로 전달받은 객체를 S3 업로드 함수에 전달한다.
     *       업로드후에는 s3에 저장된 이미지의 url 을 get 한다.
     *
     *       local 에 저장된 이미지를 삭제하는 메서드를 호출한다.
     *
     *       업로드한 이미지 정보를 가지고 있는 messageDto 를 생성해 return 한다.
     * */
    private MessageDto upload(File uploadFile, String dirName) {

        // upload image to s3
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        // delete local file
        removeNewFile(uploadFile);

        return MessageDto.builder()
                        .dirName(dirName)
                        .fileName(uploadFile.getName())
                        .keyName(fileName)
                        .imageUrl(uploadImageUrl)
                        .build();
    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : 최종적으로 amazon client 를 통해 이미지를 s3에 업로드한다.
     *
     *        업로드 후 생성된 이미지의 url 을 return 한다.
     * */
    private String putS3(File uploadFile, String fileName) {

        // upload image to s3
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();

    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : s3 업로드전 local 에 생성한 File 을 삭제한다.
     * */
    private void removeNewFile(File targetFile) {

        // remove local saved file
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }

    }

    /**
     * [ 2020-03-12 : 이민호 ]
     * 설명 : MultipartFile 을 File 로 convert 한다.
     * */
    private Optional<File> convert(MultipartFile file) throws IOException {

        // convert MultipartFile to File
        File convertFile = new File(genSerialNumber()+ "_" + file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
