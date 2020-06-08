package com.example.docxddd.application.folder.dto;

import com.example.docxddd.domain.folder.Attachment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO - Criar somente 1 UpdateIdentityDocumentDto
// Possui documentNumber e uma lista de attachments
// A lista é validada em cada documento de identificação
@NoArgsConstructor
@Getter
public class UpdateIdentityDocumentDto extends UpdateDocumentDto {

    private String documentNumber;

    private List<Attachment> attachments;
}
