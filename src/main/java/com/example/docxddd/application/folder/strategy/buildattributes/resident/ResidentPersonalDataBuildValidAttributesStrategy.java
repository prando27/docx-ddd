package com.example.docxddd.application.folder.strategy.buildattributes.resident;

import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.dto.PersonalDataAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.application.folder.strategy.buildattributes.BuildValidAttributesStrategy;
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
public class ResidentPersonalDataBuildValidAttributesStrategy
        implements BuildValidAttributesStrategy<PersonalDataAttributes> {

    @Override
    public Result<PersonalDataAttributes> build(DocumentTypeAttributesDto attributes) {
        var dto = (PersonalDataAttributesDto) attributes;

        Result<Name> fullNameResult = Name.create(dto.getFullName());
        if (fullNameResult.isError()) {
            return Result.error(fullNameResult.getError());
        }

        var builder = new PersonalDataAttributes.Builder(fullNameResult.getValue());

        if (dto.getBirthDate() != null) {
            builder.withBirthDate(dto.getBirthDate());

            if (dto.getRg() == null) {
                return Result.error("RG required");
            }
            builder.withRg(dto.getRg());

            Result<Cpf> cpfResult = Cpf.create(dto.getCpf());
            if (cpfResult.isError()) {
                return Result.error(cpfResult.getError());
            }
            builder.withCpf(cpfResult.getValue());

            if (dto.getCellPhoneNumber() == null) {
                return Result.error("Cell Phone number required");
            }
            builder.withCellPhoneNumber(dto.getCellPhoneNumber());

            Result<Email> emailResult = Email.create(dto.getEmail());
            if (emailResult.isError()) {
                return Result.error(emailResult.getError());
            }
            builder.withEmail(emailResult.getValue());
        }

        return builder.build();
    }

    @Override
    public List<DocumentType> documentTypesToApply() {
        return Collections.singletonList(DocumentType.PERSONAL_DATA);
    }

    @Override
    public List<Context> contextsToApply() {
        return Collections.singletonList(Context.RESIDENT);
    }
}
