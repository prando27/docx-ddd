package com.example.docxddd.domain.folder.entity.identity;

import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class IdentityAttributes extends DocumentTypeAttributes {

    private String documentNumber;

    private IdentityAttributes(String documentNumber) {

        if (documentNumber == null) {
            throw new IllegalArgumentException("documentNumber cannot be null");
        }

        this.documentNumber = documentNumber;
    }

    public static Result<IdentityAttributes> create(String documentNumber) {
        try {
            return Result.ok(new IdentityAttributes(documentNumber));
        } catch (IllegalArgumentException ex) {
            return Result.error(ex.getMessage());
        }
    }

    @Override
    public DocumentTypeAttributes mergeWith(DocumentTypeAttributes attributes) {
        var attrs = (IdentityAttributes) attributes;

        return new IdentityAttributes(
                ofNullable(documentNumber).orElse(attrs.documentNumber)
        );
    }
}
