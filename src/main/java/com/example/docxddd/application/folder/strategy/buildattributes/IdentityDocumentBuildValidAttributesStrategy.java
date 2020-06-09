package com.example.docxddd.application.folder.strategy.buildattributes;

import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.dto.IdentityDocumentAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.identity.IdentityAttributes;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class IdentityDocumentBuildValidAttributesStrategy implements BuildValidAttributesStrategy<IdentityAttributes> {

    @Override
    public Result<IdentityAttributes> build(DocumentTypeAttributesDto attributes) {
        var dto = (IdentityDocumentAttributesDto) attributes;

        return IdentityAttributes.create(dto.getDocumentNumber());
    }

    @Override
    public List<DocumentType> documentTypesToApply() {
        return Arrays.asList(DocumentType.CNH, DocumentType.RG, DocumentType.RNE);
    }

    @Override
    public List<Context> contextsToApply() {
        return Context.ALL;
    }
}
