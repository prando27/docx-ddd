package com.example.docxddd.application.folder.strategy.buildattributes.owner;

import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.dto.PersonalDataAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.application.folder.strategy.buildattributes.BuildAttributesStrategy;
import com.example.docxddd.domain.Cpf;
import com.example.docxddd.domain.Email;
import com.example.docxddd.domain.Name;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.personaldata.PersonalDataAttributes;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OwnerPersonalDataBuildAttributesStrategy
        implements BuildAttributesStrategy<PersonalDataAttributes> {

    @Override
    public Result<PersonalDataAttributes> create(DocumentTypeAttributesDto attributes) {
        var dto = (PersonalDataAttributesDto) attributes;

        Result<Name> fullNameResult = Name.create(dto.getFullName());
        if (fullNameResult.isError()) {
            return Result.error(fullNameResult.getError());
        }

        Result<Cpf> cpfResult = Cpf.create(dto.getCpf());
        if (cpfResult.isError()) {
            return Result.error(cpfResult.getError());
        }

        Result<Email> emailResult = Email.create(dto.getEmail());
        if (emailResult.isError()) {
            return Result.error(emailResult.getError());
        }

        return new PersonalDataAttributes.Builder(fullNameResult.getValue())
                .withEmail(emailResult.getValue())
                .withCpf(cpfResult.getValue())
                .build();
    }

    @Override
    public DocumentType documentTypeToApply() {
        return DocumentType.PERSONAL_DATA;
    }

    @Override
    public List<Context> contextsToApply() {
        return Collections.singletonList(Context.OWNER);
    }
}
