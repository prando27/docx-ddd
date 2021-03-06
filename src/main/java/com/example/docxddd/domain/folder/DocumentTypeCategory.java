package com.example.docxddd.domain.folder;

import lombok.Getter;

@Getter
public enum DocumentTypeCategory {

    DEFAULT(Constants.UNLIMITED_DOCUMENTS),
    UNIQUE(Constants.ONE_DOCUMENT);

    private final Integer numberOfDocuments;

    DocumentTypeCategory(Integer numberOfDocuments) {
        this.numberOfDocuments = numberOfDocuments;
    }

    public static class Constants {
        public static final Integer ONE_DOCUMENT = 1;
        public static final Integer UNLIMITED_DOCUMENTS = Integer.MAX_VALUE;
    }

}
