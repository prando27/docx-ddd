package com.example.docxddd.application.folder.strategy.complete;

import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

// TODO Strategy para aplicar em todos os documentos que não possuem situações especiais
@Component
public class StrictDocumentCompleteStrategy
        implements DocumentCompleteStrategy {

    @Override
    public boolean isComplete(Document document) {
        return true;
    }

    @Override
    public List<DocumentType> documentTypesToApply() {
        return Arrays.asList(
                DocumentType.BANK_DATA,
                DocumentType.BILLING_ADDRESS);
    }

    @Override
    public List<Context> contextsToApply() {
        return Context.ALL;
    }
}
