package com.example.docxddd.infrastructure.persistence.converter;

import com.example.docxddd.domain.folder.FolderType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class FolderTypeConverter implements AttributeConverter<FolderType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FolderType folderType) {
        return folderType.getId();
    }

    @Override
    public FolderType convertToEntityAttribute(Integer folderTypeId) {
        return FolderType.fromId(folderTypeId);
    }
}
