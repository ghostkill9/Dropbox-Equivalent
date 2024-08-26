package com.dropbox.demo.controller;

import com.dropbox.demo.controller.response.FileDeleteRes;
import com.dropbox.demo.controller.response.FileUploadRes;
import com.dropbox.demo.dto.FileDto;
import com.dropbox.demo.service.FileStorageService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/files")
@Slf4j
public class ApplicationController {

    @Autowired
    private  FileStorageService fileStorageService;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadRes> uploadFile(@RequestParam("file") MultipartFile file) {
            log.info("Received request for file Upload");
            FileUploadRes fileUploadRes = fileStorageService.uploadFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(fileUploadRes);

    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> readFile(@PathVariable String fileId){
            log.info("Received request for Read File");
            byte[] fileContent = fileStorageService.readFile(fileId);
            if(fileContent != null){
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileId + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(fileContent);
            }
            return ResponseEntity.internalServerError().body(null);
    }

    @GetMapping("/{file}")
    public ResponseEntity<List<FileDto>> getFileByFileName(@PathVariable String file, @RequestParam int pageNumber, @RequestParam int pageSize){
            log.info("Fetching all files meta data");
            List<FileDto> fileDtoList = fileStorageService.getAllFiles(file,pageNumber,pageSize);
            return ResponseEntity.ok()
                    .body(fileDtoList);

    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<FileDeleteRes> deleteFile(@PathVariable@NonNull String fileId){
            log.info("Request for deleting file {} received",fileId);
            FileDeleteRes fileDeleteRes = fileStorageService.deleteFile(fileId);
            return ResponseEntity.ok().body(fileDeleteRes);
    }


}
