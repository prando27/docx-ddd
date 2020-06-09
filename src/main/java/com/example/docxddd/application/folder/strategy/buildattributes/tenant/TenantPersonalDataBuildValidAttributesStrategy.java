package com.example.docxddd.application.folder.strategy.buildattributes.tenant;

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

// TODO Pensar em uma maneira do usuário passar somente os dados necessários e o docx fazer um "merge" de tudo
@Component
public class TenantPersonalDataBuildValidAttributesStrategy
        implements BuildValidAttributesStrategy<PersonalDataAttributes> {

    @Override
    public Result<PersonalDataAttributes> build(DocumentTypeAttributesDto attributes) {
        var dto = (PersonalDataAttributesDto) attributes;

        // Campos obrigatórios no contexto IQ
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

        Cpf cpf = null;
        if (dto.getCpf() != null) {
            Result<Cpf> cpfResult = Cpf.create(dto.getCpf());
            if (cpfResult.isError()) {
                return Result.error(cpfResult.getError());
            }
            cpf = cpfResult.getValue();
        }

        // TODO O uso do método create ao invés do builder garante que o
        //  usuário da classe não esqueça de passar os campos opcionais
        return PersonalDataAttributes.create(
                fullNameResult.getValue(),
                emailResult.getValue(),
                cpf,
                dto.getMaritalStatus(),
                dto.getCellPhoneNumber(),
                dto.getPhoneNumber(),
                dto.getRg(),
                dto.getBirthDate());
    }

    @Override
    public List<DocumentType> documentTypesToApply() {
        return Collections.singletonList(DocumentType.PERSONAL_DATA);
    }

    @Override
    public List<Context> contextsToApply() {
        return Collections.singletonList(Context.TENANT);
    }
}
