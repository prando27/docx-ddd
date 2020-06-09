package com.example.docxddd.application.folder.strategy.buildattributes;

import com.example.docxddd.application.folder.dto.DocumentTypeAttributesDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.bankdata.BankDataAttributes;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class BankDataBuildValidAttributesStrategy implements BuildValidAttributesStrategy<BankDataAttributes> {

    @Override
    public Result<BankDataAttributes> build(DocumentTypeAttributesDto attributes) {
        return null;
    }

    @Override
    public List<DocumentType> documentTypesToApply() {
        return Collections.singletonList(DocumentType.BANK_DATA);
    }

    @Override
    public List<Context> contextsToApply() {
        return Context.ALL;
    }
}
