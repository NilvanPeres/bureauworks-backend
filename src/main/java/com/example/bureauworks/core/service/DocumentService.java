package com.example.bureauworks.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bureauworks.core.entity.Document;
import com.example.bureauworks.core.entity.Translator;
import com.example.bureauworks.core.exception.ExceptionUtil;
import com.example.bureauworks.core.repository.DocumentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;
    private final TranslatorService translatorService;

    public Document findById(Integer id) {
        return ExceptionUtil.requireEntity(findByIdQuietly(id), "Document not found");
    }

    private Document findByIdQuietly(Integer id) {
        ExceptionUtil.requireField(id, "Id of document is required");
        return repository.findById(id).orElse(null);
    }

    public Page<Document> findAll(final Pageable pageable, final String author, final String subject) {
        return repository.findAll(pageable, author, subject);
    }

    public Document save(final Document document) {
        validateRequiredFields(document);
        getTranslator(document);

        return repository.save(document);
    }

    public Document update(final Integer id, final Document document) {
        Document documentDB = findById(id);
        validateRequiredFields(document);

        Document documentUpdated = Document.builder()
                .id(documentDB.getId())
                .author(document.getAuthor())
                .content(document.getContent())
                .subject(document.getSubject())
                .build();
        
        return repository.save(documentUpdated);
    }

    public void delete(final Integer id) {
        repository.delete(findById(id));
    }
     
    private void validateRequiredFields(Document document) {
        ExceptionUtil.requireField(document.getAuthor(), "Author info is required");
        ExceptionUtil.requireField(document.getSubject(), "Subject info is required");
        ExceptionUtil.requireField(document.getContent(), "Content info is required");
    }

    private void getTranslator(Document document) {
        Translator translator = translatorService.findByName(document.getAuthor());
        document.setTranslator(translator);
    }
}
