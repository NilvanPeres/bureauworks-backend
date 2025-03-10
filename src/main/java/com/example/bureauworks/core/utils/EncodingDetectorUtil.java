package com.example.bureauworks.core.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.mozilla.universalchardet.UniversalDetector;

public class EncodingDetectorUtil {

    private static final int PAGE_SIZE = 4096;
    private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();

    private EncodingDetectorUtil() {}

    /**
     * Detecta o encoding de um arquivo a partir de um InputStream.
     * 
     * @param inputStream o InputStream do arquivo para detectar o encoding
     * @return o encoding detectado ou UTF-8 caso não seja possível identificar
     * @throws IOException se ocorrer erro na leitura do arquivo
     */
    public static String detectEncoding(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream não pode ser nulo");
        }
        
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            final byte[] buffer = new byte[PAGE_SIZE];
            final UniversalDetector detector = new UniversalDetector(null);
            
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) > 0 && !detector.isDone()) {
                detector.handleData(buffer, 0, bytesRead);
            }
            
            detector.dataEnd();
            
            String encoding = detector.getDetectedCharset();
            
            if (encoding == null || encoding.isEmpty()) {
                return DEFAULT_ENCODING;
            }
            
            return encoding;
        } catch (IOException e) {
            throw new IOException("Erro ao ler o arquivo para detectar o encoding", e);
        }
    }

}
