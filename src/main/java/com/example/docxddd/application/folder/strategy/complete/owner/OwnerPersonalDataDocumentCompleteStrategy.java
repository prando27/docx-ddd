package com.example.docxddd.application.folder.strategy.complete.owner;

import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.application.folder.strategy.complete.DocumentCompleteStrategy;
import com.example.docxddd.domain.folder.DocumentType;
import com.example.docxddd.domain.folder.entity.Document;
import com.example.docxddd.domain.folder.entity.personaldata.PersonalDataAttributes;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class OwnerPersonalDataDocumentCompleteStrategy
        implements DocumentCompleteStrategy {

    @Override
    public boolean isComplete(Document document) {
        if (document.getAttributes().isEmpty()) {
            return false;
        }

        var attributes = (PersonalDataAttributes) document.getAttributes().get();

        return attributes.getFullName() != null
                && attributes.getCpf().isPresent()
                && attributes.getEmail().isPresent();
    }

    @Override
    public List<DocumentType> documentTypesToApply() {
        return Collections.singletonList(DocumentType.PERSONAL_DATA);
    }

    @Override
    public List<Context> contextsToApply() {
        return Collections.singletonList(Context.OWNER);
    }
}
