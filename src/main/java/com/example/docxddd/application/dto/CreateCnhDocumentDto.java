package com.example.docxddd.application.dto;

import com.example.docxddd.domain.folder.Attachment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCnhDocumentDto extends CreateDocumentDto {

    private String documentNumber;

    private Attachment attachment;
}
