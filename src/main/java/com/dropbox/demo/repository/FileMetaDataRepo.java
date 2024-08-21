package com.dropbox.demo.repository;

import com.dropbox.demo.entity.FileMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaDataRepo extends JpaRepository<FileMetaDataEntity,Long> {
    FileMetaDataEntity findByFileName(String fileName);
}
