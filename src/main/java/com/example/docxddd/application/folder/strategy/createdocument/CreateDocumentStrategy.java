package com.example.docxddd.application.folder.strategy.createdocument;

import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;

public interface CreateDocumentStrategy<T extends Document> {

    Result<T> create(CreateDocumentStrategyInput input);

    DocumentType appliesTo();
}
