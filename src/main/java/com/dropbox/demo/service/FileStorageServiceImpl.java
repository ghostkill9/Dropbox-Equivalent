package com.dropbox.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3DataSource;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.dropbox.demo.config.AmazonS3Config;
import com.dropbox.demo.controller.response.FileDeleteRes;
import com.dropbox.demo.controller.response.FileUploadRes;
import com.dropbox.demo.dto.FileDto;
import com.dropbox.demo.entity.FileMetaDataEntity;
import com.dropbox.demo.repository.FileMetaDataRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.ExceptionUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    private AmazonS3Config amazonS3Config;
    @Autowired
    private FileMetaDataRepo fileMetaDataRepo;

    //Method for Uploading/updating a file
    public FileUploadRes uploadFile( MultipartFile file)  {
        FileUploadRes fileUploadRes = new FileUploadRes();
        try{
            AmazonS3 s3Client = amazonS3Config.amazonS3();
            if(s3Client == null){
                throw new ServiceException("An error occurred connecting to S3 bucket");
            }
            if(file == null || file.getOriginalFilename() == null || file.getContentType() == null ){
                throw new ServiceException("File data is null");
            }
            s3Client.putObject(new PutObjectRequest(bucketName,file.getOriginalFilename(), convertMultipartFileToFile(file)));
            URL url = s3Client.getUrl(bucketName,file.getOriginalFilename());
            saveFileMetaData(file.getOriginalFilename(),file.getContentType(),url.toString());
            fileUploadRes.setStatus("Success");
            fileUploadRes.setMessage("File successfully uploaded");
        } catch(Exception e){
            fileUploadRes.setStatus("Failed");
            fileUploadRes.setMessage(e.getMessage());
        }
        return fileUploadRes;
    }

    //
    void saveFileMetaData(String fileName,String fileType, String s3Url){
        log.info("Saving file meta data for file {}",fileName);
        FileMetaDataEntity fileMetaDataEntity = fileMetaDataRepo.findByFileName(fileName);
        if(fileMetaDataEntity == null){
            //File doesn't exist, will create new entity
            fileMetaDataEntity = new FileMetaDataEntity();
            fileMetaDataEntity.setFileName(fileName);
            fileMetaDataEntity.setFileType(fileType);
            fileMetaDataEntity.setFileLink(s3Url);

        }
        else{
            //File exists, updating updatedAt.
            log.info("File meta data already exists, updating");
            fileMetaDataEntity.setUpdatedAt(Instant.now());
        }
        fileMetaDataRepo.save(fileMetaDataEntity);
    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {

        File tempFile = File.createTempFile("temp", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);

        return tempFile;
    }

    public byte[] readFile(String fileName){
        log.info("Getting file {}",fileName);
        try{
            AmazonS3 s3Client = amazonS3Config.amazonS3();
            S3Object s3Object = s3Client.getObject(bucketName,fileName);
            S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
            byte[] binaryContent  = IOUtils.toByteArray(s3ObjectInputStream);
            return binaryContent;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FileDto> getAllFiles() {
        log.info("Getting all files from DB");
        List<FileMetaDataEntity> fileMetaDataEntityList = fileMetaDataRepo.findAll();
        if(fileMetaDataEntityList.isEmpty()){
            return null;
        }
        List<FileDto> fileDtoList = new ArrayList<>();
        for( FileMetaDataEntity fileMetaDataEntity : fileMetaDataEntityList){
            FileDto fileDto = new FileDto();
            fileDto.setFileName(fileMetaDataEntity.getFileName());
            fileDto.setFileType(fileMetaDataEntity.getFileType());
            fileDto.setFileLink(fileMetaDataEntity.getFileLink());
            fileDto.setCreatedAt(Date.from(fileMetaDataEntity.getCreatedAt()));
            fileDtoList.add(fileDto);
        }
        return fileDtoList;
    }

    @Override
    public FileDeleteRes deleteFile(String fileName) {
        try{
            AmazonS3 s3Client = amazonS3Config.amazonS3();
            s3Client.deleteObject(bucketName,fileName);
            FileMetaDataEntity fileMetaDataEntity = fileMetaDataRepo.findByFileName(fileName);
            fileMetaDataRepo.delete(fileMetaDataEntity);
            return  new FileDeleteRes("Success","Successfully deleted the file");
        }catch (Exception e){

            e.printStackTrace();
            return new FileDeleteRes("Failed",e.getMessage());

        }
    }


}
