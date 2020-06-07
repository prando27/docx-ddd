package com.example.docxddd.domain.folder;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum FolderType {

    USER(1),
    PERSON(2),
    COMPANY(3);

    private final int id;

    FolderType(int id) {
        this.id = id;
    }

    public static FolderType fromId(int id) {
        return Stream.of(values())
                .filter(folderType -> id == folderType.getId())
                .findFirst()
                .orElse(USER);
    }
}
