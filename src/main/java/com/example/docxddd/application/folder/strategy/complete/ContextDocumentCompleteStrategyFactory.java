package com.example.docxddd.application.folder.strategy.complete;

import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.folder.DocumentType;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ContextDocumentCompleteStrategyFactory {

    private final Map<Context, Map<DocumentType, DocumentCompleteStrategy>> documentCompleteStrategiesByContext;

    private ContextDocumentCompleteStrategyFactory(List<DocumentCompleteStrategy> createStrategies) {
        documentCompleteStrategiesByContext = new HashMap<>();

        createStrategies.forEach(strategy -> {
            strategy.contextsToApply().forEach(context -> {
                var documentTypeMap = documentCompleteStrategiesByContext
                        .computeIfAbsent(context, k -> new HashMap<>());

                strategy.documentTypesToApply().forEach(documentType -> documentTypeMap.put(documentType, strategy));
            });

        });
    }

    public Optional<DocumentCompleteStrategy>
        findByContextAndDocumentType(Context context,
                                     DocumentType documentType) {

        return Optional.ofNullable(documentCompleteStrategiesByContext.get(context))
                .map(map -> map.get(documentType));
    }
}
