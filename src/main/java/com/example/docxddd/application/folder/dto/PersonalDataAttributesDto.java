package com.example.docxddd.application.folder.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class PersonalDataAttributesDto extends DocumentTypeAttributesDto {

    private String fullName;

    private String email;

    private String cpf;

    private String maritalStatus;

    private String cellPhoneNumber;

    private String phoneNumber;

    private String rg;

    private LocalDate birthDate;

}
