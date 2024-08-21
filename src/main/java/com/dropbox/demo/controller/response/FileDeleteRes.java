package com.dropbox.demo.controller.response;

import lombok.Data;

@Data
public class FileDeleteRes {
    String status;
    String message;

    public FileDeleteRes(String status,String message){
        this.status = status;
        this.message = message;
    }
}
