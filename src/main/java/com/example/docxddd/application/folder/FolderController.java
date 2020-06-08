package com.example.docxddd.application.folder;

import com.example.docxddd.application.DocumentCopyApplicationService;
import com.example.docxddd.application.TransactionalApplicationServiceController;
import com.example.docxddd.application.folder.dto.CreateDocumentDto;
import com.example.docxddd.application.folder.dto.CreateFolderDto;
import com.example.docxddd.application.folder.dto.DocumentDto;
import com.example.docxddd.application.folder.dto.FolderDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.application.folder.strategy.buildattributes.ContextBuildAttributesStrategyFactory;
import com.example.docxddd.domain.folder.entity.Document;
import com.example.docxddd.domain.folder.entity.Folder;
import com.example.docxddd.domain.folder.repository.FolderRepository;
import com.example.docxddd.infrastructure.http.dto.Envelope;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
public class FolderController
        extends TransactionalApplicationServiceController {

    private final FolderRepository folderRepository;

    private final DocumentCopyApplicationService documentCopyApplicationService;

    private final ContextBuildAttributesStrategyFactory contextBuildAttributesStrategyFactory;

    @PostMapping("/folders")
    @Transactional
    public ResponseEntity<Envelope<FolderDto>> create(@RequestBody CreateFolderDto dto) {

        var createFolderResult = Folder.create(dto.getFolderType(), dto.getExternalId());
        if (createFolderResult.isError()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error(createFolderResult.getError())));
        }
        var folder = createFolderResult.getValue();

        folderRepository.save(folder);

        return ResponseEntity.ok(Envelope.ok(toFolderDto(folder)));
    }

    @PutMapping("/folders/{folderId}/documents")
    @Transactional
    public ResponseEntity<Envelope<DocumentDto>> createDocument(@PathVariable Long folderId,
                                                                @RequestBody CreateDocumentDto dto,
                                                                @RequestHeader(defaultValue = "OWNER") String context) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error("Folder not exists")));
        }
        var folder = folderOptional.get();

        var getContextResult = Context.getContextByName(context);
        if (getContextResult.isError()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error(getContextResult.getError())));
        }

        var buildAttributesStrategyOptional = contextBuildAttributesStrategyFactory
                .findByContextAndDocumentType(getContextResult.getValue(), dto.getDocumentType());
        if (buildAttributesStrategyOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error("Document Type not exists for the specified context")));
        }
        var createAttributesStrategy = buildAttributesStrategyOptional.get();

        var documentAttributesResult = createAttributesStrategy.create(dto.getAttributes());
        if (documentAttributesResult.isError()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error(documentAttributesResult.getError())));
        }

        // TODO - Validar a existência dos arquivos de attachments (Se existir), retornar badrequest se não
        // Fazer com que somente o AttachmentService (ou algo assim) consiga devolver instâncias verificadas de attachments para criação/atualização de documentos

        var createDocumentResult = Document.create(
                dto.getDocumentType(),
                documentAttributesResult.getValue(),
                dto.getAttachments());
        if (createDocumentResult.isError()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error(createDocumentResult.getError())));
        }
        var document = createDocumentResult.getValue();

        folder.addDocument(document);

        return ResponseEntity.ok(Envelope.ok(new DocumentDto(document)));
    }

    @PutMapping("/folders/{folderId}/documents/{documentId}")
    @Transactional
    public ResponseEntity<Envelope<Void>> updateDocument(@PathVariable Long folderId,
                                                         @PathVariable Long documentId,
                                                         @RequestBody CreateDocumentDto dto,
                                                         @RequestHeader(defaultValue = "OWNER") Context context) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error("Folder not exists")));
        }
        var folder = folderOptional.get();

        var buildAttributesStrategyOptional = contextBuildAttributesStrategyFactory
                .findByContextAndDocumentType(context, dto.getDocumentType());
        if (buildAttributesStrategyOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error("Document Type not exists for the specified context")));
        }
        var createAttributesStrategy = buildAttributesStrategyOptional.get();

        var documentAttributesResult = createAttributesStrategy.create(dto.getAttributes());
        if (documentAttributesResult.isError()) {
            return rollBackResponseEntity(ResponseEntity.badRequest().body(Envelope.error(documentAttributesResult.getError())));
        }

        folder.updateDocument(documentId,
                dto.getDocumentType(),
                documentAttributesResult.getValue(),
                dto.getAttachments());

        return ResponseEntity.ok(Envelope.ok(null));
    }

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<Envelope<FolderDto>> findById(@PathVariable Long folderId) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Envelope.ok(folderOptional.map(this::toFolderDto).get()));
    }

    private FolderDto toFolderDto(Folder folder) {
        return new FolderDto(
                folder.getId(),
                folder.getExternalId(),
                folder.getFolderType(),
                folder.getLabel(),
                folder.getDocuments()
                        .stream()
                        .map(DocumentDto::new)
                        .collect(Collectors.toList()));
    }
}
