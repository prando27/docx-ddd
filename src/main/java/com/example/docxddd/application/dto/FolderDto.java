package com.example.docxddd.application.dto;

import com.example.docxddd.domain.folder.FolderType;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FolderDto {

    private final Long id;

    private final String externalId;

    private final FolderType folderType;

    private final String label;

    private final List<DocumentDto> documents;
}
