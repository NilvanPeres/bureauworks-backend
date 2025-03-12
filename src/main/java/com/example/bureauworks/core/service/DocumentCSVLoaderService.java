package com.example.bureauworks.core.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import com.example.bureauworks.core.entity.Document;
import com.example.bureauworks.core.enums.LangCountryEnum;
import com.example.bureauworks.core.service.Client.OpenAiClientService;
import com.example.bureauworks.web.exception.BureuWorksException;
import com.example.bureauworks.web.model.DocumentCSVModel;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.lang.reflect.Field;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import static com.example.bureauworks.core.utils.EncodingDetectorUtil.detectEncoding;

@Service
@RequiredArgsConstructor
@Log4j2
public class DocumentCSVLoaderService {

    private final OpenAiClientService openAiClientService;
    
@SneakyThrows
public List<Document> loadFromCSV(InputStream file) {
    log.info("Loading documents from CSV file");
    
    byte[] fileContent = file.readAllBytes();
    
    // Detectar encoding usando o array de bytes
    String encoding;
    try (InputStream encodingStream = new ByteArrayInputStream(fileContent)) {
        encoding = detectEncoding(encodingStream);
    }
    log.info("Encoding detected: {}", encoding);
    
    // Criar novo stream a partir do array de bytes para ler o CSV
    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(new ByteArrayInputStream(fileContent), encoding))) {
        
        // Configuração personalizada para capturar erros de validação
        MappingStrategy<DocumentCSVModel> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(DocumentCSVModel.class);
        
        // Configuração do OpenCSV com nossa estratégia personalizada
        CsvToBean<DocumentCSVModel> csvToBean = new CsvToBeanBuilder<DocumentCSVModel>(reader)
                .withMappingStrategy(mappingStrategy)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withSeparator(';')
                .withQuoteChar('"')
                .withEscapeChar('\\')
                .withMultilineLimit(-1)
                .build();
        
        try {
            // Tentar processar o CSV
            List<DocumentCSVModel> csvModels = csvToBean.parse();
            log.info("Found {} documents in CSV file", csvModels.size());
            
            // Converte a lista de DocumentCSVModel para lista de Document
            return convertToDocumentList(csvModels);
        } catch (BureuWorksException e) {
            // Capturar erros do OpenCSV
            if (e.getCause() instanceof CsvRequiredFieldEmptyException) {
                CsvRequiredFieldEmptyException fieldException = (CsvRequiredFieldEmptyException) e.getCause();
                
                // Obter informações sobre o erro
                long lineNumber = fieldException.getLineNumber();
                String[] missingFields = fieldException.getDestinationFields().stream()
                        .map(Field::getName)  // Usando Field::getName em vez de fieldException::getName
                        .toArray(String[]::new);
                
                // Obter a linha com problema
                String problematicLine = getLineFromFile(fileContent, encoding, lineNumber);
                
                // Montar uma mensagem de erro mais descritiva
                String errorMsg = String.format(
                        "Erro na linha %d: campos obrigatórios ausentes: %s. Conteúdo da linha: [%s]",
                        lineNumber,
                        String.join(", ", missingFields),
                        problematicLine
                );
                
                log.error(errorMsg);
                throw new BureuWorksException(errorMsg);
            } else {
                log.error("Error loading documents from CSV file", e);
                throw new RuntimeException("Failed to load documents from CSV: " + e.getMessage(), e);
            }
        }
    } catch (Exception e) {
        log.error("Error loading documents from CSV file", e);
        throw new RuntimeException("Failed to load documents from CSV: " + e.getMessage(), e);
    }
}

    /**
     * Método auxiliar para obter a linha específica do arquivo
     */
    private String getLineFromFile(byte[] fileContent, String encoding, long lineNumber) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(fileContent), encoding))) {
            
            String line;
            long currentLine = 1;
            
            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) {
                    return line;
                }
                currentLine++;
            }
            
            return "Linha não encontrada";
        } catch (Exception e) {
            return "Não foi possível obter o conteúdo da linha";
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
                .locale(parseLocale(csvModel.getLocale(), csvModel.getSubject()))
                .build();
    }
    
    /**
     * Converte a string de locale para o enum LangCountryEnum
     */
    private LangCountryEnum parseLocale(String localeStr, String subject) {
        if (StringUtils.isEmpty(localeStr)) {
            String localeFromOpenAi = openAiClientService.generateChatCompletion(subject);
            return LangCountryEnum.valueOf(localeFromOpenAi);
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
