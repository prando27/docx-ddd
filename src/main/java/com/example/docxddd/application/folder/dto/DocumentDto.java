package com.example.docxddd.application.folder.dto;

import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.example.docxddd.domain.folder.entity.Document;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@AllArgsConstructor
@Getter
public class DocumentDto {

    private final DocumentType documentType;

    private final DocumentTypeAttributes attributes;

    private final List<Attachment> attachments;

    @JsonProperty(value = "isComplete")
    private final boolean complete;

    public static DocumentDto from(Document document, boolean complete) {

        return new DocumentDto(
                document.getDocumentType(),
                document.getAttributes().orElse(null),
                document.getAttachments(),
                complete);
    }
}
