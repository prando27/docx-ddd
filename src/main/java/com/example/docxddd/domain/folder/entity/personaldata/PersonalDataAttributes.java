package com.example.docxddd.domain.folder.entity.personaldata;

import com.example.docxddd.domain.Cpf;
import com.example.docxddd.domain.Email;
import com.example.docxddd.domain.Name;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentTypeAttributes;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class PersonalDataAttributes extends DocumentTypeAttributes {

    private Name fullName;
    private Email email;
    private Cpf cpf;
    private String maritalStatus;
    private String cellPhoneNumber;
    private String phoneNumber;
    private String rg;
    private LocalDate birthDate;

    private PersonalDataAttributes(Name fullName,
                                   Email email,
                                   Cpf cpf,
                                   String maritalStatus,
                                   String cellPhoneNumber,
                                   String phoneNumber,
                                   String rg,
                                   LocalDate birthDate) {

        if (fullName == null) {
            throw new IllegalArgumentException("fullName cannot be null");
        }

        this.fullName = fullName;
        this.email = email;
        this.cpf = cpf;
        this.maritalStatus = maritalStatus;
        this.cellPhoneNumber = cellPhoneNumber;
        this.phoneNumber = phoneNumber;
        this.rg = rg;
        this.birthDate = birthDate;
    }

    @Override
    public DocumentTypeAttributes mergeWith(DocumentTypeAttributes attributes) {
        if (!attributes.getClass().isInstance(this)) {
            throw new IllegalArgumentException("attributes of different type");
        }

        var attrs = (PersonalDataAttributes) attributes;

        return new PersonalDataAttributes(
                ofNullable(fullName).orElse(attrs.fullName),
                ofNullable(email).orElse(attrs.email),
                ofNullable(cpf).orElse(attrs.cpf),
                ofNullable(maritalStatus).orElse(attrs.maritalStatus),
                ofNullable(cellPhoneNumber).orElse(attrs.cellPhoneNumber),
                ofNullable(phoneNumber).orElse(attrs.phoneNumber),
                ofNullable(rg).orElse(attrs.rg),
                ofNullable(birthDate).orElse(attrs.birthDate)
        );
    }

    public static class Builder {

        private final Name fullName;
        private Email email;
        private Cpf cpf;
        private String maritalStatus;
        private String cellPhoneNumber;
        private String phoneNumber;
        private String rg;
        private LocalDate birthDate;

        public Builder(Name fullName) {
            this.fullName = fullName;
        }

        public Builder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public Builder withCpf(Cpf cpf) {
            this.cpf = cpf;
            return this;
        }

        public Builder withMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
            return this;
        }

        public Builder withCellPhoneNumber(String cellPhoneNumber) {
            this.cellPhoneNumber = cellPhoneNumber;
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withRg(String rg) {
            this.rg = rg;
            return this;
        }

        public Builder withBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Result<PersonalDataAttributes> build() {
            try {
                return Result.ok(new PersonalDataAttributes(
                        fullName,
                        email,
                        cpf,
                        maritalStatus,
                        cellPhoneNumber,
                        phoneNumber,
                        rg,
                        birthDate));
            } catch (IllegalArgumentException ex) {
                return Result.error(ex.getMessage());
            }
        }
    }
}
