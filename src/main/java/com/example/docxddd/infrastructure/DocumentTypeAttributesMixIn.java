package com.example.docxddd.infrastructure;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class DocumentTypeAttributesMixIn {

    @JsonIgnore
    String clazz;
}
