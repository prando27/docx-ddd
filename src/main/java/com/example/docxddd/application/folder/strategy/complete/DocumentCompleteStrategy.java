package com.example.docxddd.application.folder.strategy.complete;

import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;

import java.util.List;

public interface DocumentCompleteStrategy {

    boolean isComplete(Document document);

    List<DocumentType> documentTypesToApply();

    List<Context> contextsToApply();
}
