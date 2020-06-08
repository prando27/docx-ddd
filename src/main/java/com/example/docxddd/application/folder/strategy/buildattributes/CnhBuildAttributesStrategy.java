package com.example.docxddd.application.folder.strategy.buildattributes;

import com.example.docxddd.application.folder.dto.CnhAttributesDto;
import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.identity.IdentityAttributes;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CnhBuildAttributesStrategy implements BuildAttributesStrategy<IdentityAttributes> {

    @Override
    public Result<IdentityAttributes> create(DocumentTypeAttributesDto attributes) {
        var dto = (CnhAttributesDto) attributes;

        return IdentityAttributes.create(dto.getDocumentNumber());
    }

    @Override
    public DocumentType documentTypeToApply() {
        return DocumentType.CNH;
    }

    @Override
    public List<Context> contextsToApply() {
        return Context.ALL;
    }

//    private final DocumentCopyApplicationService documentCopyApplicationService;

//    @Override
//    public Result<CnhDocument> create(CreateDocumentStrategyInput input) {
//        var dto = (CreateCnhDocumentDto) input.getCreateDocumentDto();
//
////        var documentCopyOptional = documentCopyApplicationService
////                .findDocumentCopy(
////                        input.getFolderId(),
////                        dto.getFileName());
//
////        if (documentCopyOptional.isEmpty()) {
////            return Result.error("FileName not exists, upload a document copy first");
////        }
//
////        return CnhDocument.create(dto.getDocumentNumber(), dto.getAttachment());
//    }
//
//    @Override
//    public DocumentType appliesTo() {
//        return DocumentType.CNH;
//    }
}
