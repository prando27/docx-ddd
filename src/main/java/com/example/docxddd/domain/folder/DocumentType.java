package com.example.docxddd.domain.folder;

import com.example.docxddd.domain.folder.entity.Document;
import com.example.docxddd.domain.folder.entity.identity.IdentityAttributes;
import com.example.docxddd.domain.folder.entity.personaldata.PersonalDataAttributes;

import lombok.Getter;

import java.util.function.Function;
import java.util.stream.Stream;

import static com.example.docxddd.domain.folder.Attachment.ONE_PAGE;
import static com.example.docxddd.domain.folder.Attachment.TWO_PAGES;
import static com.example.docxddd.domain.folder.Attachment.ZERO_PAGES;

// TODO Uniquiness de documento PERSONAL_DATA = DocumentType, RG, RNH, RNE = Category, BANK_DATA, BILLING_ADDRESS = DocumentType+Content
// Somente para adicionar novos documentos, não interfere na atualização
// A atualização deve validar caso 1 pois pode estar trocando o documento para um conteúdo que já existe
// 1. getUniquenessKey = DocumentType+Attributes+Attachments = BANK_DATA, BILLING_ADDRESS, PROCURACAO
// 2. getUniquenessKey = DocumentType = PERSONAL_DATA
// 3. getUniquenessKey = DocumentType+Category = RG,CNH,RNE (Troca de tipo funciona?)

// Alterando os dados de um bank data com os dados iguais de um outro bank data
// nesse caso, validar se existe um id diferente com o mesmo uniqueness, se tiver fodeu
@Getter
public enum DocumentType {

    PERSONAL_DATA(1,
            PersonalDataAttributes.class,
            ZERO_PAGES,
            UniquenessFunctions.DOCUMENT_TYPE_UNIQUENESS),

    BILLING_ADDRESS(2,
            null,
            ZERO_PAGES,
            UniquenessFunctions.TYPE_ATTRIBUTES_ATTACHMENTS_UNIQUENESS),

    BANK_DATA(3,
            null,
            ZERO_PAGES,
            UniquenessFunctions.TYPE_ATTRIBUTES_ATTACHMENTS_UNIQUENESS),

    RG(4,
            IdentityAttributes.class,
            TWO_PAGES,
            UniquenessFunctions.IDENTITY),

    CNH(5,
            IdentityAttributes.class,
            ONE_PAGE,
            UniquenessFunctions.IDENTITY),

    RNE(6,
            IdentityAttributes.class,
            TWO_PAGES,
            UniquenessFunctions.IDENTITY);

    private final int id;
    private final Class<? extends DocumentTypeAttributes> attributesClass;
    private final int numberOfAttachments;
    private final Function<Document, String> uniquenessKeyFunction;

    DocumentType(int id,
                 Class<? extends DocumentTypeAttributes> attributesClass,
                 Integer numberOfAttachments,
                 Function<Document, String> uniquenessKeyFunction) {
        this.id = id;
        this.attributesClass = attributesClass;
        this.numberOfAttachments = numberOfAttachments;
        this.uniquenessKeyFunction = uniquenessKeyFunction;
    }

    public static DocumentType fromId(int id) {
        return Stream.of(values())
                .filter(documentType -> id == documentType.getId())
                .findFirst()
                .orElse(PERSONAL_DATA);
    }

    public static class UniquenessFunctions {
        public static final Function<Document, String> DOCUMENT_TYPE_UNIQUENESS = document -> document.getDocumentType().toString();

        public static final Function<Document, String> IDENTITY = document -> "IDENTITY";

        public static final Function<Document, String> TYPE_ATTRIBUTES_ATTACHMENTS_UNIQUENESS = document -> document.getDocumentType().toString()
                + document.getAttributes().map(Object::toString).orElse("")
                + document.getAttachments().toString();
    }

}
