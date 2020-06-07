package com.example.docxddd.infrastructure;

import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class DocumentMixIn {

    @JsonIgnoreProperties(value = { "clazz" })
    DocumentTypeAttributes attributes;
}
