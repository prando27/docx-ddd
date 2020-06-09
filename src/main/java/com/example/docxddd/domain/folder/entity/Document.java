package com.example.docxddd.domain.folder.entity;

import com.example.docxddd.domain.common.BaseEntity;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.example.docxddd.infrastructure.config.jackson.ObjectMapperConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.docxddd.domain.folder.Attachment.ONE_PAGE;
import static com.example.docxddd.domain.folder.Attachment.ZERO_PAGES;

// TODO - Modelo deve comportar todos os dados (OWNER, TENANT e RESIDENT)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
@Table(name = "document")
public class Document extends BaseEntity {

    // TODO - Criar o ObjectMapper uma vez só para toda a aplicação
    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperConfig.createObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    // TODO - Remover esse setter e conseguir já criar o documento com o folderId
    // TODO - Definir como PACKAGE faz com que somente Folder possa usa-lo!
    @Setter(AccessLevel.PACKAGE)
    @ManyToOne
    @JoinColumn(name = "folder_id", nullable = false)
    private Folder folder;

    @Column(name = "document_type_id", nullable = false)
    @Getter
    private DocumentType documentType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Attachment> attachments;

    private Map<String, Object> toMapAttributes(Object attributes) {
        return OBJECT_MAPPER.convertValue(attributes, MAP_TYPE_REFERENCE);
    }

    private <T extends DocumentTypeAttributes> T toDocumentTypeAttributes(Class<T> documentTypeAttributeClass) {
        return OBJECT_MAPPER.convertValue(attributes, documentTypeAttributeClass);
    }

    // Sempre que um getter for nullable, retornar Optional
    public Optional<DocumentTypeAttributes> getAttributes() {
        if (attributes != null) {
            return Optional.of(toDocumentTypeAttributes(documentType.getAttributesClass()));
        }
        return Optional.empty();
    }

    public List<Attachment> getAttachments() {
        return Optional.ofNullable(attachments)
                .map(Collections::unmodifiableList)
                .orElseGet(() -> Collections.unmodifiableList(Collections.emptyList()));
    }

    public boolean isNotSameDocumentType(DocumentType documentType) {
        return !this.documentType.equals(documentType);
    }

    public String getUniquenessKey() {
        return documentType.getUniquenessKeyFunction().apply(this);
    }

    private Document(DocumentType documentType,
                     DocumentTypeAttributes attributes,
                     List<Attachment> attachments) {
        checkDocumentInvariants(documentType, attributes, attachments);

        this.documentType = documentType;
        this.attributes = attributes != null ? toMapAttributes(attributes) : null;
        this.attachments = attachments;
    }

    private void checkDocumentInvariants(DocumentType documentType,
                                         DocumentTypeAttributes attributes,
                                         List<Attachment> attachments) {

        if (documentType == null) {
            throw new IllegalArgumentException("DocumentType cannot be null");
        }

        if (attributes != null
                && !documentType.getAttributesClass().isInstance(attributes)) {
            throw new IllegalArgumentException("DocumentType: "
                    + documentType.name() + " should use "
                    + documentType.getAttributesClass().getSimpleName());
        }

        if (documentType.getNumberOfAttachments() != ZERO_PAGES
                && (attachments == null || attachments.size() != documentType.getNumberOfAttachments())) {
            throw new IllegalArgumentException("DocumentType: " + documentType.name()
                    + " require " + documentType.getNumberOfAttachments()
                    + (documentType.getNumberOfAttachments() == ONE_PAGE ? " attachment" : " attachments"));
        }

        if (attachments != null
                && (attachments.stream()
                    .map(Attachment::getPageNumber)
                    .anyMatch(pageNumber -> pageNumber > attachments.size()))) {

                throw new IllegalArgumentException("Attachment pageNumber"
                        + " cannot be higher than the number required attachments");
        }
    }

    public static Result<Document> create(DocumentType documentType,
                                          DocumentTypeAttributes attributes,
                                          List<Attachment> attachments) {
        try {
            return Result.ok(new Document(documentType, attributes, attachments));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    public Result<Void> changeDocumentType(DocumentType documentType,
                                           DocumentTypeAttributes attributes,
                                           List<Attachment> attachments) {
        try {
            checkDocumentInvariants(
                    documentType,
                    attributes,
                    attachments);

            this.documentType = documentType;
            this.attributes = attributes != null ? toMapAttributes(attributes) : null;
            this.attachments = attachments;

            return Result.ok(null);
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    public Result<Void> update(DocumentTypeAttributes attributes,
                               List<Attachment> attachments) {
        try {
            checkDocumentInvariants(
                    this.documentType,
                    attributes,
                    attachments);

            if (attributes != null) {
                this.attributes = toMapAttributes(attributes);
            }

            this.attachments = attachments;

            return Result.ok(null);
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }
}
