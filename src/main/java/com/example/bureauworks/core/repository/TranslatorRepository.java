package com.example.bureauworks.core.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bureauworks.core.entity.Translator;

public interface TranslatorRepository extends JpaRepository<Translator, Integer>{

    @Query("""
        SELECT t
        FROM Translator t
        WHERE (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')))
                AND (:email IS NULL OR LOWER(t.email) LIKE LOWER(CONCAT('%', :email, '%'))) 
        """)
    Page<Translator> findAll(Pageable pageable, String name, String email);

    @Query("SELECT t FROM Translator t where (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Optional<Translator> findByName(String name);

    @Query("SELECT t FROM Translator t where (:email IS NULL OR LOWER(t.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Optional<Translator> findByEmail(String email);

    @Query("""
        select exists (
            select 1
            from Translator t
            where LOWER(t.email) LIKE LOWER(CONCAT('%', :email, '%'))
                and (:id is null OR t.id <> id)
        )     
    """)
    boolean isEmailAlreadyInUse(String email, Integer id);
    
}
