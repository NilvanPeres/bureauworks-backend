package com.example.bureauworks.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bureauworks.core.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer>  {

    @Query("""
          SELECT d
          FROM Document d
          WHERE d.deleted = false
            AND (:author IS NULL OR LOWER(d.author) LIKE LOWER(CONCAT('%', :author, '%'))) 
            AND (:subject IS NULL OR LOWER (d.subject) LIKE LOWER(CONCAT('%', :subject, '%'))) 
    """)
    Page<Document> findAll(Pageable pageable, String author, String subject);
    
}
