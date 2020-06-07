package com.example.docxddd.application.dto;

import com.example.docxddd.domain.folder.FolderType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateFolderDto {

    private String externalId;

    private FolderType folderType;
}
