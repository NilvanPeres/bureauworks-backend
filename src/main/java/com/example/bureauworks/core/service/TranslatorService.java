package com.example.bureauworks.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bureauworks.core.entity.Translator;
import com.example.bureauworks.core.exception.EntityNotFoundException;
import com.example.bureauworks.core.exception.ExceptionUtil;
import com.example.bureauworks.core.repository.TranslatorRepository;

import lombok.RequiredArgsConstructor;

import static com.example.bureauworks.core.utils.EmailUtil.isValidEmail;

@Service
@RequiredArgsConstructor
public class TranslatorService {

    private final TranslatorRepository repository;

    public Translator findById(Integer id) {
        return ExceptionUtil.requireEntity(findByIdQuietly(id), "Translator not found");
    }

    private Translator findByIdQuietly(Integer id) {
        ExceptionUtil.requireField(id, "Id of translator cannot be null");
        return repository.findById(id).orElse(null);
    }

    public Translator findByName(final String author) {
        return repository.findByName(author)
                .orElseThrow(() -> new EntityNotFoundException("Translator not found"));
    }

    public Translator findByEmail(String author) {
        return repository.findByEmail(author)
                .orElseThrow(() -> new EntityNotFoundException("Translator not found"));
    }

    public Page<Translator> findAll(final Pageable pageable, final String name, final String email) {
        return repository.findAll(pageable, name , email);
    }

    public Translator save(Translator translator) {
        validateRequiredFields(translator);
        return repository.save(translator);
    }

    public Translator update(Integer translatorId, Translator translator) {
        Translator translatorDD = findById(translatorId);
        validateRequiredFields(translator);

        Translator updatedTranslator = Translator.builder()
            .id(translatorDD.getId())
            .email(translator.getEmail())
            .name(translator.getName())
            .sourceLanguage(translator.getSourceLanguage())
            .targetLanguage(translator.getTargetLanguage())
            .build();

        return repository.save(updatedTranslator);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
        
    private void validateRequiredFields(Translator translator) {
        ExceptionUtil.requireField(translator.getName(), "Name of translator must be informed");
        ExceptionUtil.requireField(translator.getEmail(), "Email of translator must be informed");
        ExceptionUtil.requireField(translator.getTargetLanguage(), "Source language of translator must be informed");

        if (!isValidEmail(translator.getEmail())) {
            ExceptionUtil.exception("Invalid email");
        }
    }    
}
