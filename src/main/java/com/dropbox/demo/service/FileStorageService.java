package com.dropbox.demo.service;

import com.dropbox.demo.controller.response.FileDeleteRes;
import com.dropbox.demo.controller.response.FileUploadRes;
import com.dropbox.demo.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileStorageService {
    FileUploadRes uploadFile(MultipartFile file);

    byte[] readFile(String fileName);

    List<FileDto> getAllFiles(String file, int pageNumber, int pageSize);

    FileDeleteRes deleteFile(String fileName);
}
