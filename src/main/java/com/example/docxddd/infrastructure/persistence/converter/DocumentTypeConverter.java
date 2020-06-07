package com.example.docxddd.infrastructure.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.example.docxddd.domain.folder.DocumentType;

@Converter(autoApply = true)
public class DocumentTypeConverter implements AttributeConverter<DocumentType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DocumentType documentType) {
        return documentType.getId();
    }

    @Override
    public DocumentType convertToEntityAttribute(Integer documentTypeId) {
        return DocumentType.fromId(documentTypeId);
    }
}
