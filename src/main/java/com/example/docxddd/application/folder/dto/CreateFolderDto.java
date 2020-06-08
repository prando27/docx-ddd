package com.example.docxddd.application.folder.dto;

import com.example.docxddd.domain.folder.FolderType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateFolderDto {

    private String externalId;

    private FolderType folderType;
}
