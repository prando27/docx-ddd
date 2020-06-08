package com.example.docxddd.application.folder.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdatePersonalDataDocumentDto extends UpdateDocumentDto {

    private String fullName;

    private String cpf;

    private String email;
}
