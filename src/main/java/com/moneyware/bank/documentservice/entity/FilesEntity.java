package com.moneyware.bank.documentservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "files")
public class FilesEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Lob
    @JsonIgnore
    private byte[] data;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "filesize")
    private long fileSize;

    @Column(name = "timestamp")
    private LocalDate timeStamp;

    @Column
    private String customerId;

    @Column
    private String documentType;

    @Column
    private String status;


    public FilesEntity(String fileName, String customerId) {
        this.fileName = fileName;
        this.customerId = customerId;
    }

    public FilesEntity(String fileName, long fileSize, LocalDate timeStamp, String customerId, String documentType, String status, byte[] data) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.timeStamp = timeStamp;
        this.customerId = customerId;
        this.documentType = documentType;
        this.status = status;
        this.data = data;
    }
}
