package com.example.docxddd.application.dto;

import com.example.docxddd.domain.folder.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "documentType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreatePersonalDataDocumentDto.class, name = "PERSONAL_DATA"),
        @JsonSubTypes.Type(value = CreateCnhDocumentDto.class, name = "CNH")
})
public abstract class CreateDocumentDto {

    private DocumentType documentType;
}
