package com.example.docxddd.application.folder.strategy.createdocument;

import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CreateDocumentStrategyFactory {

    private final Map<DocumentType, CreateDocumentStrategy<? extends Document>> createStrategyByDocumentType;

    private CreateDocumentStrategyFactory(List<CreateDocumentStrategy<? extends Document>> createStrategies) {
        createStrategyByDocumentType = new HashMap<>();

        createStrategies.forEach(createDocumentStrategy ->
                createStrategyByDocumentType.put(
                        createDocumentStrategy.appliesTo(),
                        createDocumentStrategy));
    }

    public Optional<CreateDocumentStrategy<? extends Document>> findCreateStrategyByDocumentType(DocumentType documentType) {
        return Optional.ofNullable(createStrategyByDocumentType.get(documentType));
    }
}
