package com.example.bureauworks.web.model;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class DocumentCSVModel {
    
    @CsvBindByName(column = "subject", required = true)
    private String subject;
    
    @CsvBindByName(column = "content", required = true)
    private String content;
    
    @CsvBindByName(column = "locale")
    private String locale;
    
    @CsvBindByName(column = "author", required = true)
    private String author;
}
