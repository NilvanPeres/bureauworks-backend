package com.example.bureauworks.core.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.bureauworks.core.entity.Document;
import com.example.bureauworks.core.enums.LangCountryEnum;
import com.example.bureauworks.core.utils.EncodingDetectorUtil;
import com.example.bureauworks.web.model.DocumentCSVModel;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import static com.example.bureauworks.core.utils.EncodingDetectorUtil.detectEncoding;

@Service
@RequiredArgsConstructor
@Log4j2
public class DocumentCSVLoaderService {
    
    private final TranslatorService translatorService;

    @SneakyThrows
    public List<Document> loadFromCSV(InputStream file) {
        log.info("Loading documents from CSV file");
        
        byte[] fileContent = file.readAllBytes();
        
        // Detectar encoding usando o array de bytes
        String encoding;
        try (InputStream encodingStream = new ByteArrayInputStream(fileContent)) {
            encoding = EncodingDetectorUtil.detectEncoding(encodingStream);
        }
        log.info("Encoding detected: {}", encoding);
        
        // Criar novo stream a partir do array de bytes para ler o CSV
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(fileContent), encoding))) {
            
            // Configuração do OpenCSV para mapear o CSV para DocumentCSVModel
            CsvToBean<DocumentCSVModel> csvToBean = new CsvToBeanBuilder<DocumentCSVModel>(reader)
                    .withType(DocumentCSVModel.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .withSeparator(';') // Define ponto e vírgula como separador conforme especificado no Sample Document
                    .withQuoteChar('"')       // Define o caractere de citação
                    .withEscapeChar('\\')     // Define o caractere de escape
                    .withMultilineLimit(-1)   // Multinha
                    .build();
            
            // Realiza a leitura e transformação do CSV para lista de DocumentCSVModel
            List<DocumentCSVModel> csvModels = csvToBean.parse();
            log.info("Found {} documents in CSV file", csvModels.size());
            
            // Converte a lista de DocumentCSVModel para lista de Document
            return convertToDocumentList(csvModels);
        } catch (Exception e) {
            log.error("Error loading documents from CSV file", e);
            throw new RuntimeException("Failed to load documents from CSV: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converte uma lista de DocumentCSVModel para lista de Document
     */
    private List<Document> convertToDocumentList(List<DocumentCSVModel> csvModels) {
        return csvModels.stream()
                .map(this::convertToDocument)
                .toList();
    }
    
    /**
     * Converte um DocumentCSVModel para Document
     * TODO: tratar para verificar existência de translator
     */
    private Document convertToDocument(DocumentCSVModel csvModel) {
        // Validação dos campos obrigatórios
        if (StringUtils.isEmpty(csvModel.getSubject()) || StringUtils.isEmpty(csvModel.getContent())) {
            log.warn("Skipping document with empty subject or content: {}", csvModel);
            return null;
        }
        
        return Document.builder()
                .subject(csvModel.getSubject())
                .content(csvModel.getContent())
                .author(csvModel.getAuthor())
                .locale(parseLocale(csvModel.getLocale()))
                .build();
    }
    
    /**
     * Converte a string de locale para o enum LangCountryEnum
     */
    private LangCountryEnum parseLocale(String localeStr) {
        if (StringUtils.isEmpty(localeStr)) {
            // TODO: Chamar API OPENAI para detectar o idioma do texto
            return null;
        }

        String normalizedLocale = localeStr.replace("-", "_").toUpperCase();
        
        try {
            return LangCountryEnum.valueOf(normalizedLocale);
        } catch (IllegalArgumentException e) {
            // TODO: tratar erro dps. Adicionando valor default para testes
            return null;
        }
    }
}
