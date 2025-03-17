package com.example.bureauworks.web.model;

import com.example.bureauworks.core.entity.Document;
import com.example.bureauworks.core.enums.LangCountryEnum;

import lombok.Data;

@Data
public class DocumentPageable {

    private Integer id;
    private String author;
    private String subject;
    private LangCountryEnum locale;
     
    public DocumentPageable(Document document) {
        this.id = document.getId();
        this.author = document.getAuthor();
        this.subject = document.getSubject();
        this.locale = document.getLocale();
    }
    
}
