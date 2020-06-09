package com.example.docxddd.application.folder.dto;

import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
public class CreateDocumentDto {

    private DocumentType documentType;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "documentType",
            visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = PersonalDataAttributesDto.class, name = "PERSONAL_DATA"),
            @JsonSubTypes.Type(value = IdentityDocumentAttributesDto.class, name = "CNH"),
            @JsonSubTypes.Type(value = IdentityDocumentAttributesDto.class, name = "RG")
    })
    private DocumentTypeAttributesDto attributes;

    private List<Attachment> attachments;

    public List<Attachment> attachments() {
        return Optional
                .ofNullable(attachments)
                .orElseGet(Collections::emptyList);
    }
}
