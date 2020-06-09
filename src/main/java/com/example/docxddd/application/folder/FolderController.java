package com.example.docxddd.application.folder;

import com.example.docxddd.application.DocumentCopyApplicationService;
import com.example.docxddd.application.TransactionalApplicationServiceController;
import com.example.docxddd.application.folder.dto.CreateDocumentDto;
import com.example.docxddd.application.folder.dto.CreateFolderDto;
import com.example.docxddd.application.folder.dto.DocumentDto;
import com.example.docxddd.application.folder.dto.FolderDto;
import com.example.docxddd.application.folder.strategy.Context;
import com.example.docxddd.application.folder.strategy.buildattributes.ContextBuildAttributesStrategyFactory;
import com.example.docxddd.application.folder.strategy.complete.ContextDocumentCompleteStrategyFactory;
import com.example.docxddd.application.folder.strategy.complete.DocumentCompleteStrategy;
import com.example.docxddd.domain.common.Result;
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

import java.util.Optional;
import java.util.stream.Collectors;

// TODO Tentar implementar Railroad oriented programming nessa camada!
@AllArgsConstructor
@RestController
public class FolderController
        extends TransactionalApplicationServiceController {

    private final FolderRepository folderRepository;

    private final DocumentCopyApplicationService documentCopyApplicationService;

    private final ContextBuildAttributesStrategyFactory contextBuildAttributesStrategyFactory;

    private final ContextDocumentCompleteStrategyFactory contextDocumentCompleteStrategyFactory;

    @PostMapping("/folders")
    @Transactional
    public ResponseEntity<Envelope<FolderDto>> create(@RequestBody CreateFolderDto dto) {

        var createFolderResult = Folder.create(dto.getFolderType(), dto.getExternalId());
        if (createFolderResult.isError()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error(createFolderResult.getError())));
        }
        var folder = createFolderResult.getValue();

        folderRepository.save(folder);

        return ResponseEntity.ok(Envelope.ok(toFolderDto(folder, null, null)));
    }

    @PutMapping("/folders/{folderId}/documents")
    @Transactional
    public ResponseEntity<Envelope<DocumentDto>> createDocument(
            @PathVariable Long folderId,
            @RequestBody CreateDocumentDto dto,
            @RequestHeader(defaultValue = "OWNER") String context) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error("Folder not exists")));
        }
        var folder = folderOptional.get();

        var getContextResult = Context.getContextByName(context);
        if (getContextResult.isError()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error(getContextResult.getError())));
        }

        var buildAttributesStrategyOptional = contextBuildAttributesStrategyFactory
                .findByContextAndDocumentType(getContextResult.getValue(), dto.getDocumentType());
        if (buildAttributesStrategyOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error("Document Type not exists for the specified context")));
        }
        var createAttributesStrategy = buildAttributesStrategyOptional.get();

        var documentAttributesResult = createAttributesStrategy.build(dto.getAttributes());
        if (documentAttributesResult.isError()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error(documentAttributesResult.getError())));
        }

        // TODO - Validar a existência dos arquivos de attachments (Se existir), retornar badrequest se não
        // Fazer com que somente o AttachmentService (ou algo assim) consiga devolver instâncias verificadas de attachments para criação/atualização de documentos
        // Fazer com que na construção de um Document com attachments, uma interface de validação seja recebida para validar a existência dos mesmos e já retornar algum erro
        // Isso impede que alguem use a classe e crie um documento com attachments que não existem

        var createDocumentResult = Document.create(
                dto.getDocumentType(),
                documentAttributesResult.getValue(),
                dto.getAttachments());
        if (createDocumentResult.isError()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error(createDocumentResult.getError())));
        }
        var document = createDocumentResult.getValue();

        var addDocumentResult = folder.addDocument(document);
        if (addDocumentResult.isError()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error(addDocumentResult.getError())));
        }

        // TODO Melhorar essa chamada do completestrategy, esta mto feio isso!
        Optional<DocumentCompleteStrategy> byContextAndDocumentType = contextDocumentCompleteStrategyFactory
                .findByContextAndDocumentType(getContextResult.getValue(), dto.getDocumentType());

        var outDto = DocumentDto.from(document, byContextAndDocumentType.map(st -> st.isComplete(document)).orElse(false));
        return ResponseEntity.ok(Envelope.ok(outDto));
    }

    @PutMapping("/folders/{folderId}/documents/{documentId}")
    @Transactional
    public ResponseEntity<Envelope<DocumentDto>> updateDocument(
            @PathVariable Long folderId,
            @PathVariable Long documentId,
            @RequestBody CreateDocumentDto dto,
            @RequestHeader(defaultValue = "OWNER") Context context) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error("Folder not exists")));
        }
        var folder = folderOptional.get();

        var buildAttributesStrategyOptional = contextBuildAttributesStrategyFactory
                .findByContextAndDocumentType(context, dto.getDocumentType());
        if (buildAttributesStrategyOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error("Document Type not exists for the specified context")));
        }
        var buildAttributesStrategy = buildAttributesStrategyOptional.get();

        var documentOptional = folder.findDocumentById(documentId);
        if (documentOptional.isEmpty()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error("Document not found")));
        }
        var document = documentOptional.get();

        var documentAttributesResult = buildAttributesStrategy.build(dto.getAttributes());
        if (documentAttributesResult.isError()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error(documentAttributesResult.getError())));
        }

        Result<Void> updateDocumentResult = null;
        if (document.isNotSameDocumentType(dto.getDocumentType())) {
            updateDocumentResult = document.changeDocumentType(
                    dto.getDocumentType(),
                    documentAttributesResult.getValue(),
                    dto.getAttachments());
        } else {
            updateDocumentResult = document.update(
                    documentAttributesResult.getValue(),
                    dto.getAttachments());
        }

        if (updateDocumentResult.isError()) {
            return rollBackResponseEntity(ResponseEntity
                    .badRequest()
                    .body(Envelope.error(updateDocumentResult.getError())));
        }

        // TODO Melhorar essa chamada do completestrategy, esta mto feio isso!
        Optional<DocumentCompleteStrategy> byContextAndDocumentType = contextDocumentCompleteStrategyFactory
                .findByContextAndDocumentType(context, dto.getDocumentType());

        var outDto = DocumentDto.from(document, byContextAndDocumentType.map(st -> st.isComplete(document)).orElse(false));
        return ResponseEntity.ok(Envelope.ok(outDto));
    }

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<Envelope<FolderDto>> findById(@PathVariable Long folderId,
                                                        @RequestHeader(defaultValue = "OWNER") Context context) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // TODO Melhorar essa chamada do completestrategy, esta mto feio isso!
        return ResponseEntity.ok(
                Envelope.ok(folderOptional.map(folder -> toFolderDto(folder, context, contextDocumentCompleteStrategyFactory)).get()));
    }

    private FolderDto toFolderDto(Folder folder,
                                  Context context,
                                  ContextDocumentCompleteStrategyFactory factory) {

        // TODO Melhorar essa chamada do completestrategy, esta mto feio isso!
        return new FolderDto(
                folder.getId(),
                folder.getExternalId(),
                folder.getFolderType(),
                folder.getLabel(),
                folder.getDocuments()
                        .stream()
                        .map(document -> DocumentDto.from(document, context != null && factory.findByContextAndDocumentType(context, document.getDocumentType()).map(st -> st.isComplete(document)).orElse(false)))
                        .collect(Collectors.toList()));
    }
}
