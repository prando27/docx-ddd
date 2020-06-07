package com.example.docxddd.application.folder.strategy.updatedocument;

import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UpdateDocumentStrategyFactory {

    private final Map<DocumentType, UpdateDocumentStrategy<? extends Document>> updateStrategyByDocumentType;

    private UpdateDocumentStrategyFactory(List<UpdateDocumentStrategy<? extends Document>> updateStrategies) {
        updateStrategyByDocumentType = new HashMap<>();

        updateStrategies.forEach(updateDocumentStrategy ->
                updateStrategyByDocumentType.put(
                        updateDocumentStrategy.appliesTo(),
                        updateDocumentStrategy));
    }

    public Optional<UpdateDocumentStrategy<? extends Document>> findUpdateStrategyByDocumentType(DocumentType documentType) {
        return Optional.ofNullable(updateStrategyByDocumentType.get(documentType));
    }
}
