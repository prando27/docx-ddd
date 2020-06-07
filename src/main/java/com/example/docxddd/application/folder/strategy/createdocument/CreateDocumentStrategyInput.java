package com.example.docxddd.application.folder.strategy.createdocument;

import com.example.docxddd.application.dto.CreateDocumentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateDocumentStrategyInput {

    private final Long folderId;

    private final CreateDocumentDto createDocumentDto;
}
