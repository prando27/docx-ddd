package com.example.docxddd.application.folder.strategy.buildattributes;

import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.bankdata.BankDataAttributes;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankDataBuildAttributesStrategy implements BuildAttributesStrategy<BankDataAttributes> {

    @Override
    public Result<BankDataAttributes> create(DocumentTypeAttributesDto attributes) {
        return null;
    }

    @Override
    public DocumentType documentTypeToApply() {
        return DocumentType.BANK_DATA;
    }

    @Override
    public List<Context> contextsToApply() {
        return Context.ALL;
    }
}
