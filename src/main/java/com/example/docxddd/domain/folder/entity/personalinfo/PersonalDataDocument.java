package com.example.docxddd.domain.folder.entity.personalinfo;

import com.example.docxddd.domain.Cpf;
import com.example.docxddd.domain.Email;
import com.example.docxddd.domain.Name;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;
import com.example.docxddd.domain.folder.MaxDocumentTypePerFolder;
import com.example.docxddd.domain.folder.entity.Document;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue(DocumentType.Constants.PERSONAL_DATA)
public class PersonalDataDocument extends Document {

    private PersonalDataDocument(Name fullName,
                                 Cpf cpf,
                                 Email email) {

        if (fullName == null) {
            throw new IllegalArgumentException("FullName cannot be null");
        }

        if (cpf == null) {
            throw new IllegalArgumentException("Cpf cannot be null");
        }

        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        this.attributes = super.toMapAttributes(new PersonalDataAttributes(fullName.getValue(), cpf.getValue(), email.getValue()));
    }

    @Override
    public PersonalDataAttributes getAttributes() {
        return super.toDocumentTypeAttributes(PersonalDataAttributes.class);
    }

    @Override
    public MaxDocumentTypePerFolder getMaxDocumentTypePerFolder() {
        return MaxDocumentTypePerFolder.ONE;
    }

    @Override
    public String getContent() {
        return getAttributes().toString();
    }

    public static Result<PersonalDataDocument> create(Name fullName,
                                                      Cpf cpf,
                                                      Email email) {
        try {
            return Result.ok(new PersonalDataDocument(fullName, cpf, email));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    public static class PersonalDataAttributes implements DocumentTypeAttributes {

        private String fullName;

        private String cpf;

        private String email;
    }
}
