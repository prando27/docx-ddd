package com.example.docxddd.application.folder;

import com.example.docxddd.application.TransactionalApplicationService;
import com.example.docxddd.application.dto.CreateDocumentDto;
import com.example.docxddd.application.dto.DocumentDto;
import com.example.docxddd.application.folder.strategy.createdocument.CreateDocumentStrategyFactory;
import com.example.docxddd.application.folder.strategy.createdocument.CreateDocumentStrategyInput;
import com.example.docxddd.domain.folder.repository.FolderRepository;
import com.example.docxddd.infrastructure.http.dto.Envelope;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class FolderController2 extends TransactionalApplicationService {

    private final FolderRepository folderRepository;

    private final CreateDocumentStrategyFactory createDocumentStrategyFactory;

    @PutMapping("/folders/{folderId}/documents")
    @Transactional
    public ResponseEntity<Envelope<DocumentDto>> createDocument(@PathVariable Long folderId,
                                                                @RequestBody CreateDocumentDto dto) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error("Folder not exists")));
        }
        var folder = folderOptional.get();

        var createDocumentStrategyOptional = createDocumentStrategyFactory
                .findCreateStrategyByDocumentType(dto.getDocumentType());
        if (createDocumentStrategyOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error("Document Type not exists")));
        }

        var createDocumentResult = createDocumentStrategyOptional.get().create(
                new CreateDocumentStrategyInput(folder.getId(), dto));
        if (createDocumentResult.isError()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error(createDocumentResult.getError())));
        }

        var document = createDocumentResult.getValue();
        var addDocumentResult = folder.addDocument(document);
        if (addDocumentResult.isError()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error(addDocumentResult.getError())));
        }
        if (!addDocumentResult.getValue()) {
            return rollBackResponseEntity(ResponseEntity.status(HttpStatus.CONFLICT).body(Envelope.error("Document with the same content already exists")));
        }

        return ResponseEntity.ok(Envelope.ok(new DocumentDto(document)));
    }
}
