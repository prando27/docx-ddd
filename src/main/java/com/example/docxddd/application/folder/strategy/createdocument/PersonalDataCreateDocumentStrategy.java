package com.example.docxddd.application.folder.strategy.createdocument;

import com.example.docxddd.application.dto.CreatePersonalDataDocumentDto;
import com.example.docxddd.domain.Cpf;
import com.example.docxddd.domain.Email;
import com.example.docxddd.domain.Name;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.personalinfo.PersonalDataDocument;

import org.springframework.stereotype.Component;

@Component
public class PersonalDataCreateDocumentStrategy
        implements CreateDocumentStrategy<PersonalDataDocument> {

    @Override
    public Result<PersonalDataDocument> create(CreateDocumentStrategyInput input) {
        var dto = (CreatePersonalDataDocumentDto) input.getCreateDocumentDto();

        Result<Name> fullNameResult = Name.create(dto.getFullName());
        if (fullNameResult.isError()) {
            return Result.error(fullNameResult.getError());
        }

        // Se tiver muitos campos opcionais, fazer um builder
        Result<Cpf> cpfResult = Cpf.create(dto.getCpf());
        if (cpfResult.isError()) {
            return Result.error(cpfResult.getError());
        }

        Result<Email> emailResult = Email.create(dto.getEmail());
        if (emailResult.isError()) {
            return Result.error(emailResult.getError());
        }

        return PersonalDataDocument.create(
                fullNameResult.getValue(),
                cpfResult.getValue(),
                emailResult.getValue());
    }

    @Override
    public DocumentType appliesTo() {
        return DocumentType.PERSONAL_DATA;
    }
}
