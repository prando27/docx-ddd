package com.example.docxddd.domain.folder.entity;

import com.example.docxddd.domain.common.BaseEntity;
import com.example.docxddd.domain.folder.Attachment;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.example.docxddd.domain.folder.MaxDocumentTypePerFolder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "document")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "document_type_id")
public abstract class Document extends BaseEntity {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    @ManyToOne
    @JoinColumn(name = "folder_id")
    @Setter
    private Folder folder;

    @Column(name = "document_type_id", insertable = false, updatable = false)
    @Getter
    private DocumentType documentType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    protected Map<String, Object> attributes;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    protected List<Attachment> attachments;

    protected Map<String, Object> toMapAttributes(Object attributes) {
        return OBJECT_MAPPER.convertValue(attributes, MAP_TYPE_REFERENCE);
    }

    protected <T extends DocumentTypeAttributes> T toDocumentTypeAttributes(Class<T> documentTypeAttributeClass) {
        return OBJECT_MAPPER.convertValue(attributes, documentTypeAttributeClass);
    }

    public abstract DocumentTypeAttributes getAttributes();

    public abstract MaxDocumentTypePerFolder getMaxDocumentTypePerFolder();

    public abstract String getContent();
}
