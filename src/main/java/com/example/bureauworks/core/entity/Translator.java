package com.example.bureauworks.core.entity;

import java.io.Serial;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.bureauworks.core.enums.LangCountryEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Schema(name = "Translator")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Table(name = "translator")
@SQLDelete(sql = "UPDATE Translator SET deleted = true WHERE id = ?")
public class Translator extends BaseEntity<Integer> {

    @Serial
    private static final long serialversionUID = 1283129837198981275L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id of translator. Generated at insert on database.")
    @EqualsAndHashCode.Include
    private Integer id;

    @Schema(description = "Name of translator.")
    private String name;

    @Schema(description = "Email of translator.")
    private String email;

    @Schema(description = "Source language of current document that is being translated.")
    @Enumerated(EnumType.STRING)
    private LangCountryEnum sourceLanguage;

    @Schema(description = "Target language of current document that is being translated.")
    @Enumerated(EnumType.STRING)
    private LangCountryEnum targetLanguage;

}
