package com.example.bureauworks.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bureauworks.core.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer>  {
    
}
