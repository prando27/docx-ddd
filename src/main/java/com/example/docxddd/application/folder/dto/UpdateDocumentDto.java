package com.example.docxddd.application.folder.dto;

import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public abstract class UpdateDocumentDto {

    private DocumentType documentType;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "documentType")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = UpdatePersonalDataDocumentDto.class, name = "PERSONAL_DATA"),
            @JsonSubTypes.Type(value = UpdateIdentityDocumentDto.class, name = "CNH"),

    })
    private DocumentTypeAttributesDto attributes;

    private List<Attachment> attachments;
}
