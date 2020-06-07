package com.example.docxddd.domain.folder.entity.identitydocument;

import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.example.docxddd.domain.folder.MaxDocumentTypePerFolder;
import com.example.docxddd.domain.folder.entity.Document;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class IdentityDocument extends Document {

    private static final String IDENTITY_DOCUMENT_CONTENT = "IDENTITY_DOCUMENT";

    // TODO - Pensar no que é importante para o código Java e o que é importante para a API REST
    // TODO - Talvez valha a pena mudar isso para uma lista de Attachment e manter um padrão
    public List<Attachment> getAttachment() {
        return attachments;
    }

    @Override
    public MaxDocumentTypePerFolder getMaxDocumentTypePerFolder() {
        return MaxDocumentTypePerFolder.ONE;
    }

    // Ideia: Usar EQUALS e HASHCODE para saber se é o mesmo objeto
    // Resolução: Não para não ferrar com o mecanismo do Hibernate
    @Override
    public String getContent() {
        return IDENTITY_DOCUMENT_CONTENT;
    }

    // Construtor necessário para a deserialização funcionar
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class IdentityDocumentAttributes implements DocumentTypeAttributes {

        private String documentNumber;

        protected IdentityDocumentAttributes(String documentNumber) {

            if (documentNumber == null) {
                throw new IllegalArgumentException("documentNumber cannot be null");
            }

            if (documentNumber.length() == 0) {
                throw new IllegalArgumentException("documentNumber cannot be empty");
            }

            this.documentNumber = documentNumber;
        }
    }
}
