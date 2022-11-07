package com.moneyware.bank.documentservice.repository;

import com.moneyware.bank.documentservice.entity.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FilesEntity, String> {
}
