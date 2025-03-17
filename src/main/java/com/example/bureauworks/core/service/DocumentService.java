package com.example.bureauworks.core.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.bureauworks.core.entity.Document;
import com.example.bureauworks.core.entity.Translator;
import com.example.bureauworks.core.exception.ExceptionUtil;
import com.example.bureauworks.core.repository.DocumentRepository;
import com.example.bureauworks.web.exception.BureuWorksException;
import com.example.bureauworks.web.model.DocumentPageable;

import lombok.RequiredArgsConstructor;

import static com.example.bureauworks.core.utils.IsFileExtensionValid.isFileExtensionValid;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository repository;
    private final TranslatorService translatorService;
    private final DocumentCSVLoaderService documentCSVLoader;

    public Document findById(Integer id) {
        return ExceptionUtil.requireEntity(findByIdQuietly(id), "Document not found");
    }

    private Document findByIdQuietly(Integer id) {
        ExceptionUtil.requireField(id, "Id of document is required");
        return repository.findById(id).orElse(null);
    }

    public Page<DocumentPageable> findAll(final Pageable pageable, final String author, final String subject) {
        return repository.findAll(pageable, author, subject)
                .map(DocumentPageable::new);
    }

    public Document save(final Document document) {
        validateRequiredFields(document);
        getTranslator(document);

        return repository.save(document);
    }

    public Document update(final Integer id, final Document document) {
        Document documentDB = findById(id);
        validateRequiredFields(document);
        getTranslator(document);

        Document documentUpdated = Document.builder()
                .id(documentDB.getId())
                .author(document.getAuthor())
                .content(document.getContent())
                .subject(document.getSubject())
                .locale(document.getLocale())
                .translator(document.getTranslator())
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
        Translator translator = translatorService.findByEmail(document.getAuthor());
        document.setTranslator(translator);
    }

    public void insertBatch(MultipartFile file) {
        if (!isFileExtensionValid(file.getOriginalFilename())) 
            throw new BureuWorksException("Invalid file extension. Only CSV files are allowed.");
        
        try {
            final List<Document> documents = documentCSVLoader.loadFromCSV(file.getInputStream());

            documents.stream()
                    .filter(Objects::nonNull)
                    .map(this::save)
                    .toList();

        } catch (IOException e) {
            throw new BureuWorksException("Error loading documents from CSV file: " + e);
        }

    }
}
