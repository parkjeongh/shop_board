package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {

        UUID uuid = UUID.randomUUID();  // Universally Unique Identifier - 램덤 이름 부여
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));   // 확장자 추출
        String savedFileName = uuid.toString() + extension; // 랜덤이름 + 확장자

        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;   // 업로드 된 파일 이름 반환
    }

    public void deleteFile(String filePath) throws Exception {

        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
