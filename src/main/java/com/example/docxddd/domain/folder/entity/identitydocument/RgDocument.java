package com.example.docxddd.domain.folder.entity.identitydocument;

import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(DocumentType.Constants.RG)
public class RgDocument extends IdentityDocument {

    private RgDocument(IdentityDocumentAttributes attributes,
                       List<Attachment> attachments) {

        if (attributes == null) {
            throw new IllegalArgumentException("CnhAttributes cannot be null");
        }

        if (attachments == null) {
            throw new IllegalArgumentException("Attachments cannot be null");
        }

        if (attachments.size() != 2) {
            throw new IllegalArgumentException("There must be two attachments");
        }

        if (!Attachment.PAGE_ONE.equals(attachments.get(0).getPageNumber())) {
            throw new IllegalArgumentException("First Attachment page number must be " +
                    Attachment.PAGE_ONE);
        }

        if (!Attachment.PAGE_TWO.equals(attachments.get(1).getPageNumber())) {
            throw new IllegalArgumentException("Second Attachment page number must be " +
                    Attachment.PAGE_TWO);
        }

        this.attributes = super.toMapAttributes(attributes);
        this.attachments = attachments;
    }

    @Override
    public IdentityDocumentAttributes getAttributes() {
        return super.toDocumentTypeAttributes(IdentityDocumentAttributes.class);
    }

    public static Result<RgDocument> create(String documentNumber,
                                            List<Attachment> attachments) {
        try {
            return Result.ok(new RgDocument(new IdentityDocumentAttributes(documentNumber), attachments));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    public static Result<IdentityDocument> createFrom(Long documentId,
                                                      String documentNumber,
                                                      List<Attachment> attachments) {
        try {
            RgDocument rgDocument = new RgDocument(new IdentityDocumentAttributes(documentNumber), attachments);
            rgDocument.setId(documentId);
            return Result.ok(rgDocument);
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }
}
