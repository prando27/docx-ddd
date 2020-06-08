package com.example.docxddd.domain.folder;

import com.example.docxddd.domain.folder.entity.identity.IdentityAttributes;
import com.example.docxddd.domain.folder.entity.personaldata.PersonalDataAttributes;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

import static com.example.docxddd.domain.folder.Attachment.ONE_PAGE;
import static com.example.docxddd.domain.folder.Attachment.TWO_PAGES;
import static com.example.docxddd.domain.folder.Attachment.ZERO_PAGES;

@Getter
public enum DocumentType {

    PERSONAL_DATA(1, PersonalDataAttributes.class, null),
    BILLING_ADDRESS(2, null, null),
    BANK_DATA(3, null, null),
    RG(4, null, TWO_PAGES),
    CNH(5, IdentityAttributes.class, ONE_PAGE),
    RNE(6, null, TWO_PAGES);

    private final int id;
    private final Class<? extends DocumentTypeAttributes> documentTypeClazz;
    private final Integer numberOfPages;

    DocumentType(int id,
                 Class<? extends DocumentTypeAttributes> documentTypeClazz,
                 Integer numberOfPages) {
        this.id = id;
        this.documentTypeClazz = documentTypeClazz;
        this.numberOfPages = Optional.ofNullable(numberOfPages).orElse(ZERO_PAGES);
    }

    public static DocumentType fromId(int id) {
        return Stream.of(values())
                .filter(documentType -> id == documentType.getId())
                .findFirst()
                .orElse(PERSONAL_DATA);
    }

    public static class Constants {
        public static final String RG = "4";
        public static final String CNH = "5";
    }
}
