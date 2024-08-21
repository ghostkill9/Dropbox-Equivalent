package com.dropbox.demo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FileDto {
    String fileName;
    String fileType;
    String fileLink;
    Date createdAt;

}
