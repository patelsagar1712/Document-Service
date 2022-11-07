package com.moneyware.bank.documentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "files")
public class FilesEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Lob
    private byte[] data;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "filesize")
    private long fileSize;

    @Column(name = "timestamp")
    private String timeStamp;

    @Column
    private int customerId;

    @Column
    private String documentType;

    @Column
    private String status;

    public FilesEntity(String fileName, long fileSize, String timeStamp, int customerId, String documentType, String status, byte[] data) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.timeStamp = timeStamp;
        this.customerId = customerId;
        this.documentType = documentType;
        this.status = status;
        this.data = data;
    }
}
