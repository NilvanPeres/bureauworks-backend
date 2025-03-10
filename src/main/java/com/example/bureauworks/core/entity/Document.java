package com.example.bureauworks.core.entity;

import java.io.Serial;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.bureauworks.core.enums.LangCountryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Document")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(name = "Document")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@SQLRestriction(value = "deleted = false")
@SQLDelete(sql = "UPDATE Document SET deleted = true WHERE id = ?")
public class Document extends BaseEntity <Integer> {

    @Serial
    private static final long serialVersionUID = 128937129837143432L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id of translator. Generated at insert on database.")
    @EqualsAndHashCode.Include
    private Integer id;

    @Schema(description = "Author of document.")
    private String author;

    @Schema(description = "Type of subject of current document.")
    private String subject;

    @Schema(description = "Content of current document.")
    private String content;

    @Schema(description = "In which language the document is written.")
    @Enumerated(EnumType.STRING)
    private LangCountryEnum locale;

    @ManyToOne
    @JoinColumn(name = "translator")
    @Schema(description = "Translator of current document.")
    private Translator translator;
    
}
