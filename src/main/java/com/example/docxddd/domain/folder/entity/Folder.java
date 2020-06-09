package com.example.docxddd.domain.folder.entity;

import com.example.docxddd.domain.common.AggregateRoot;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.example.docxddd.domain.folder.FolderType;
import com.example.docxddd.domain.folder.entity.personaldata.PersonalDataAttributes;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Table(name = "folder")
public class Folder extends AggregateRoot {

    //================================================================================
    // Properties
    //================================================================================

    private FolderType folderType;

    private String externalId;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Document> documents;

    //================================================================================
    // Accessors
    //================================================================================


    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    // TODO CONCEITO de label nas folders para facilitar a visualização futura
    // TODO Nada mais é do que o nome que existe no PERSONAL_DATA da folder
    public String getLabel() {
        return documents.stream()
                .filter(document -> DocumentType.PERSONAL_DATA.equals(document.getDocumentType()))
                .findFirst()
                .flatMap(Document::getAttributes)
                .map(attributes -> (PersonalDataAttributes) attributes)
                .map(attributes -> attributes.getFullName().getValue())
                .orElse("No Label");
    }

    //================================================================================
    // Constructors
    //================================================================================

    protected Folder() {
        this.documents = new ArrayList<>();
    }

    private Folder(FolderType folderType,
                   String externalId) {
        this();

        if (folderType == null) {
            throw new IllegalArgumentException("FolderType cannot be null");
        }

        this.externalId = externalId;
        this.folderType = folderType;
    }

    //================================================================================
    // Static Factory
    //================================================================================

    public static Result<Folder> create(FolderType folderType,
                                        String externalId) {
        try {
            return Result.ok(new Folder(folderType, externalId));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    //================================================================================
    // Business logic
    //================================================================================

    public Result<Boolean> addDocument(Document document) {

        boolean documentAlreadyExists = documents.stream()
                .map(Document::getUniquenessKey)
                .anyMatch(uniquenessKey -> document.getUniquenessKey().equals(uniquenessKey));
        if (documentAlreadyExists) {
            return Result.error("Document already exists");
        }

        document.setFolder(this);
        return Result.ok(documents.add(document));
    }

    public Result<Void> updateDocument(Long documentId,
                                       DocumentType documentType,
                                       DocumentTypeAttributes attributes,
                                       List<Attachment> attachments) {

        var documentOptional = findDocumentById(documentId);
        if (documentOptional.isEmpty()) {
            return Result.error("Document not found");
        }
        var existingDocument = documentOptional.get();

        Result<Void> updateDocumentResult = null;
        if (!documentType.equals(existingDocument.getDocumentType())) {
            updateDocumentResult = existingDocument.changeDocumentType(
                    documentType,
                    attributes,
                    attachments);
        } else {
            updateDocumentResult = existingDocument.update(
                    attributes,
                    attachments);
        }

        if (updateDocumentResult.isError()) {
            return Result.error(updateDocumentResult.getError());
        }

        boolean documentWithSameUniquenessKeyExists = documents.stream()
                .filter(d -> !documentId.equals(d.getId()))
                .anyMatch(d -> existingDocument.getUniquenessKey().equals(d.getUniquenessKey()));
        if (documentWithSameUniquenessKeyExists) {
            return Result.error("Document already exists");
        }

        return updateDocumentResult;

    }

    public Result<Boolean> removeDocumentById(Long documentId) {

        if (documentId == null) {
            return Result.error("documentId cannot be null");
        }

        boolean documentRemoved = documents.removeIf(document -> documentId.equals(document.getId()));

        return Result.ok(documentRemoved);
    }

    public Optional<Document> findDocumentById(Long documentId) {
        return documents.stream().filter(document -> document.getId().equals(documentId)).findFirst();
    }
}
