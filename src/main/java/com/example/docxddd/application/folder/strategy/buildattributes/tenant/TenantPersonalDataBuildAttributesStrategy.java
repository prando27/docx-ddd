package com.example.docxddd.application.folder.strategy.buildattributes.tenant;

import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.dto.PersonalDataAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.application.folder.strategy.buildattributes.BuildAttributesStrategy;
import com.example.docxddd.domain.Email;
import com.example.docxddd.domain.Name;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.personaldata.PersonalDataAttributes;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TenantPersonalDataBuildAttributesStrategy
        implements BuildAttributesStrategy<PersonalDataAttributes> {

    @Override
    public Result<PersonalDataAttributes> create(DocumentTypeAttributesDto attributes) {
        var dto = (PersonalDataAttributesDto) attributes;

        Result<Name> fullNameResult = Name.create(dto.getFullName());
        if (fullNameResult.isError()) {
            return Result.error(fullNameResult.getError());
        }

        if (dto.getMaritalStatus() == null) {
            return Result.error("Marital Status required");
        }

        if (dto.getCellPhoneNumber() == null) {
            return Result.error("Cell Phone number required");
        }

        if (dto.getPhoneNumber() == null) {
            return Result.error("Phone number required");
        }

        Result<Email> emailResult = Email.create(dto.getEmail());
        if (emailResult.isError()) {
            return Result.error(emailResult.getError());
        }

        return new PersonalDataAttributes.Builder(fullNameResult.getValue())
                .withEmail(emailResult.getValue())
                .withMaritalStatus(dto.getMaritalStatus())
                .withCellPhoneNumber(dto.getCellPhoneNumber())
                .withPhoneNumber(dto.getPhoneNumber())
                .build();
    }

    @Override
    public DocumentType documentTypeToApply() {
        return DocumentType.PERSONAL_DATA;
    }

    @Override
    public List<Context> contextsToApply() {
        return Collections.singletonList(Context.TENANT);
    }
}
