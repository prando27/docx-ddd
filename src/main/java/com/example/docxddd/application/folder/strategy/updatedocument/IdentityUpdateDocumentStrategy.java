package com.example.docxddd.application.folder.strategy.updatedocument;

import com.example.docxddd.application.dto.UpdateIdentityDocumentDto;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.identitydocument.CnhDocument;
import com.example.docxddd.domain.folder.entity.identitydocument.IdentityDocument;
import com.example.docxddd.domain.folder.entity.identitydocument.RgDocument;

import org.springframework.stereotype.Component;

// TODO - Problema: DiscriminatorColumn não suporta nativamente o um update, tem que ser um native update
// TODO - Considerar a criação de um DocumentType IDENTITY com subtipos RG, CNH e RNE
@Component
public class IdentityUpdateDocumentStrategy implements UpdateDocumentStrategy<IdentityDocument> {

    @Override
    public Result<IdentityDocument> update(UpdateDocumentStrategyInput input) {
        var dto = (UpdateIdentityDocumentDto) input.getDto();

        DocumentType documentType = dto.getDocumentType();
        if (input.getExistingDocument().getDocumentType() != documentType) {
            switch (documentType) {
                case CNH:
                    return CnhDocument.createFrom(
                            input.getExistingDocument().getId(),
                            dto.getDocumentNumber(),
                            dto.getAttachments().get(0));
                case RG:
                    return RgDocument.createFrom(
                            input.getExistingDocument().getId(),
                            dto.getDocumentNumber(),
                            dto.getAttachments());
            }
        }

//        var documentCopyOptional = documentCopyApplicationService
//                .findDocumentCopy(
//                        input.getFolderId(),
//                        dto.getFileName());

//        if (documentCopyOptional.isEmpty()) {
//            return Result.error("FileName not exists, upload a document copy first");
//        }

//        return CnhDocument.create(dto.getDocumentNumber(), dto.getAttachment());
        return null;
    }

    @Override
    public DocumentType appliesTo() {
        return DocumentType.RG;
    }
}
