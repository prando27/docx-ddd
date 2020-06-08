package com.example.docxddd.application.folder.strategy.buildattributes;

import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;

import java.util.List;

public interface BuildAttributesStrategy<T extends DocumentTypeAttributes> {

    Result<T> create(DocumentTypeAttributesDto attributes);

    DocumentType documentTypeToApply();

    List<Context> contextsToApply();
}
