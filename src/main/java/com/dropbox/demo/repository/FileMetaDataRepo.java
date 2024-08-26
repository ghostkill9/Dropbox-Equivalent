package com.dropbox.demo.repository;

import com.dropbox.demo.entity.FileMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetaDataRepo extends JpaRepository<FileMetaDataEntity,Long> {
    FileMetaDataEntity findByFileName(String fileName);

    @Query(value = "SELECT * FROM file_meta_data f WHERE f.file_name LIKE %:fileName% LIMIT :pageSize OFFSET (:pageNumber) * :pageSize", nativeQuery = true)
    List<FileMetaDataEntity> findSimilarByFileName(String fileName,int pageSize,int pageNumber);

}
