package com.example.docxddd.domain.folder.entity;

import com.example.docxddd.domain.common.AggregateRoot;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.FolderType;
import com.example.docxddd.domain.folder.MaxDocumentTypePerFolder;
import com.example.docxddd.domain.folder.entity.personalinfo.PersonalDataDocument;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    private FolderType folderType;

    private String externalId;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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
                .filter(document -> document instanceof PersonalDataDocument)
                .findFirst()
                .map(document -> (PersonalDataDocument) document)
                .map(PersonalDataDocument::getAttributes)
                .map(PersonalDataDocument.PersonalDataAttributes::getFullName)
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

    // TODO - Lógica de não permitir documentos repetidos
    public Result<Boolean> addDocument(Document document) {
        if (document == null) {
            return Result.error("document cannot be null");
        }

        boolean doesDocumentExists = isDocumentWithSameContentExists(document.getContent());
        if (doesDocumentExists) {
            if (document.getMaxDocumentTypePerFolder() == MaxDocumentTypePerFolder.ONE) {
                return Result.ok(Boolean.FALSE);
            }
        }

        document.setFolder(this);
        documents.add(document);

        return Result.ok(Boolean.TRUE);
    }

    private boolean isDocumentWithSameContentExists(String documentContent) {
        return documents.stream()
                .map(Document::getContent)
                .anyMatch(content -> content.equals(documentContent));
    }

    public Result<Void> updateDocument(Long documentId,
                                       Document document) {
        return Result.ok(null);
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
