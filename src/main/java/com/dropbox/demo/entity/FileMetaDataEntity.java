package com.dropbox.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "file_meta_data")
public class FileMetaDataEntity {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private UUID fileID;

        @Column(name = "file_name")
        private String fileName;

        @Column(name = "file_link")
        private String fileLink;

        @Column(name = "file_type")
        private String fileType;

        @Column(name = "created_at")
        private Instant createdAt;

        @Column(name = "updated_at")
        private Instant updatedAt;


        @PrePersist
        void onCreate() {
                this.createdAt = Instant.now();
                this.updatedAt = Instant.now();
        }

}
