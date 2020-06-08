package com.example.docxddd.domain.folder;

import com.example.docxddd.domain.folder.entity.identity.IdentityAttributes;
import com.example.docxddd.domain.folder.entity.personaldata.PersonalDataAttributes;

import lombok.Getter;

import java.util.stream.Stream;

import static com.example.docxddd.domain.folder.Attachment.ONE_PAGE;
import static com.example.docxddd.domain.folder.Attachment.TWO_PAGES;
import static com.example.docxddd.domain.folder.Attachment.ZERO_PAGES;

@Getter
public enum DocumentType {

    PERSONAL_DATA(1,
            DocumentTypeCategory.GENERAL,
            PersonalDataAttributes.class,
            ZERO_PAGES),

    BILLING_ADDRESS(2,
            DocumentTypeCategory.GENERAL,
            null,
            ZERO_PAGES),

    BANK_DATA(3,
            DocumentTypeCategory.GENERAL,
            null,
            ZERO_PAGES),

    RG(4,
            DocumentTypeCategory.IDENTITY,
            IdentityAttributes.class,
            TWO_PAGES),

    CNH(5,
            DocumentTypeCategory.IDENTITY,
            IdentityAttributes.class,
            ONE_PAGE),

    RNE(6,
            DocumentTypeCategory.IDENTITY,
            IdentityAttributes.class,
            TWO_PAGES);

    private final int id;
    private final DocumentTypeCategory category;
    private final Class<? extends DocumentTypeAttributes> documentTypeClass;
    private final Integer numberOfAttachments;

    DocumentType(int id,
                 DocumentTypeCategory category,
                 Class<? extends DocumentTypeAttributes> documentTypeClass,
                 Integer numberOfAttachments) {
        this.id = id;
        this.category = category;
        this.documentTypeClass = documentTypeClass;
        this.numberOfAttachments = numberOfAttachments;
    }

    public static DocumentType fromId(int id) {
        return Stream.of(values())
                .filter(documentType -> id == documentType.getId())
                .findFirst()
                .orElse(PERSONAL_DATA);
    }
}
