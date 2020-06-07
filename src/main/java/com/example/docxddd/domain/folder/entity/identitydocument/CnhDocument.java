package com.example.docxddd.domain.folder.entity.identitydocument;

import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(DocumentType.Constants.CNH)
public class CnhDocument extends IdentityDocument {

    private CnhDocument(IdentityDocumentAttributes attributes,
                        Attachment attachment) {

        if (attributes == null) {
            throw new IllegalArgumentException("CnhAttributes cannot be null");
        }

        if (attachment == null) {
            throw new IllegalArgumentException("Attachment cannot be null");
        }

        if (!Attachment.PAGE_ONE.equals(attachment.getPageNumber())) {
            throw new IllegalArgumentException("Attachment page number must be 1");
        }

        this.attributes = super.toMapAttributes(attributes);
        this.attachments = Collections.singletonList(attachment);
    }

    @Override
    public IdentityDocumentAttributes getAttributes() {
        return super.toDocumentTypeAttributes(IdentityDocumentAttributes.class);
    }

    public static Result<CnhDocument> create(String documentNumber,
                                             Attachment attachment) {
        try {
            return Result.ok(new CnhDocument(new IdentityDocumentAttributes(documentNumber), attachment));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    public static Result<IdentityDocument> createFrom(Long documentId,
                                                      String documentNumber,
                                                      Attachment attachment) {
        try {
            CnhDocument cnhDocument = new CnhDocument(
                    new IdentityDocumentAttributes(documentNumber), attachment);
            cnhDocument.setId(documentId);
            return Result.ok(cnhDocument);
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }
}
