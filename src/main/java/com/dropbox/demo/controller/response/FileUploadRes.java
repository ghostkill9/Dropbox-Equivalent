package com.dropbox.demo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadRes {
    String status;
    String message;


    public FileUploadRes() {

    }
}
