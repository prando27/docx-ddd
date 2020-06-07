package com.example.docxddd.application.folder.strategy.createdocument;

import com.example.docxddd.application.dto.CreateCnhDocumentDto;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.identitydocument.CnhDocument;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CnhCreateDocumentStrategy implements CreateDocumentStrategy<CnhDocument> {

//    private final DocumentCopyApplicationService documentCopyApplicationService;

    @Override
    public Result<CnhDocument> create(CreateDocumentStrategyInput input) {
        var dto = (CreateCnhDocumentDto) input.getCreateDocumentDto();

//        var documentCopyOptional = documentCopyApplicationService
//                .findDocumentCopy(
//                        input.getFolderId(),
//                        dto.getFileName());

//        if (documentCopyOptional.isEmpty()) {
//            return Result.error("FileName not exists, upload a document copy first");
//        }

        return CnhDocument.create(dto.getDocumentNumber(), dto.getAttachment());
    }

    @Override
    public DocumentType appliesTo() {
        return DocumentType.CNH;
    }
}
