package com.example.docxddd.application.folder.strategy.updatedocument;

import com.example.docxddd.application.dto.UpdateDocumentDto;
import com.example.docxddd.domain.folder.entity.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateDocumentStrategyInput {

    private final UpdateDocumentDto dto;

    private final Document existingDocument;
}
