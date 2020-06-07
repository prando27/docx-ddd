package com.example.docxddd.domain;

import com.example.docxddd.domain.common.Result;

import br.com.caelum.stella.validation.InvalidStateException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Email {

    private String value;

    private Email(String value) {
        this();

        if (value == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        // Perform email validation

        this.value = value;
    }

    public static Result<Email> create(String value) {
        try {
            return Result.ok(new Email(value));
        } catch (InvalidStateException ex) {
            return Result.error("Invalid Email");
        }
    }
}
