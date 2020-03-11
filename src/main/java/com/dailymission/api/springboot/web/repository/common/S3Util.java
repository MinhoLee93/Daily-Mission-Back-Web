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
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * ex) 202003090000
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
     * 설명 : MultipartFile 로 전달받은 객체를 File 로 Convert 하고 S3에 업로드 한다.
     *       업로드후에는 이미지 리사이징을 위한 MessageDto 를 생성해 return 한다.
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

    private MessageDto upload(File uploadFile, String dirName) {

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

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
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
