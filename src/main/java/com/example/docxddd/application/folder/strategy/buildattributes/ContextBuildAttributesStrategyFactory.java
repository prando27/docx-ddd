package com.example.docxddd.application.folder.strategy.buildattributes;

import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ContextBuildAttributesStrategyFactory {

    private final Map<Context, Map<DocumentType, BuildValidAttributesStrategy<? extends DocumentTypeAttributes>>> buildAttributesStrategyMapByContext;

    private ContextBuildAttributesStrategyFactory(List<BuildValidAttributesStrategy<? extends DocumentTypeAttributes>> createStrategies) {
        buildAttributesStrategyMapByContext = new HashMap<>();

        createStrategies.forEach(strategy -> {
            strategy.contextsToApply().forEach(context -> {
                var documentTypeMap = buildAttributesStrategyMapByContext
                        .computeIfAbsent(context, k -> new HashMap<>());

                strategy.documentTypesToApply().forEach(documentType -> documentTypeMap.put(documentType, strategy));
            });

        });
    }

    public Optional<BuildValidAttributesStrategy<? extends DocumentTypeAttributes>>
        findByContextAndDocumentType(Context context,
                                     DocumentType documentType) {

        return Optional.ofNullable(buildAttributesStrategyMapByContext.get(context))
                .map(map -> map.get(documentType));
    }
}
