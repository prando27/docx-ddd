package com.example.docxddd.application.folder.strategy.updatedocument;

import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;

public interface UpdateDocumentStrategy<T extends Document> {

    Result<T> update(UpdateDocumentStrategyInput input);

    DocumentType appliesTo();
}
