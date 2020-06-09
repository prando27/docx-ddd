package com.example.docxddd.application.folder.strategy.complete;

import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class IdentityDocumentCompleteStrategy
        implements DocumentCompleteStrategy {

    @Override
    public boolean isComplete(Document document) {
        // Como os documentos RG, CNH e RNE nunca podem ser criados sem documentNumber, não precisamos validar os attributes
        return document.getAttachments().size() == document.getDocumentType().getNumberOfAttachments();
    }

    @Override
    public List<DocumentType> documentTypesToApply() {
        return Arrays.asList(
                DocumentType.RG,
                DocumentType.CNH,
                DocumentType.RNE);
    }

    @Override
    public List<Context> contextsToApply() {
        return Context.ALL;
    }
}
