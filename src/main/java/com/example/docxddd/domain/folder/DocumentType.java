package com.example.docxddd.domain.folder;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum DocumentType {

    PERSONAL_DATA(1),
    BILLING_ADDRESS(2),
    BANK_DATA(3),
    RG(4),
    CNH(5),
    RNE(6);

    private final int id;

    DocumentType(int id) {
        this.id = id;
    }

    public static DocumentType fromId(int id) {
        return Stream.of(values())
                .filter(documentType -> id == documentType.getId())
                .findFirst()
                .orElse(PERSONAL_DATA);
    }

    public static class Constants {
        public static final String PERSONAL_DATA = "1";
        public static final String RG = "4";
        public static final String CNH = "5";
    }
}
