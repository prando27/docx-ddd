package com.example.docxddd.application;

import com.example.docxddd.application.dto.CreateDocumentDto;
import com.example.docxddd.application.dto.CreateFolderDto;
import com.example.docxddd.application.dto.DocumentDto;
import com.example.docxddd.application.dto.FolderDto;
import com.example.docxddd.application.dto.UpdateDocumentDto;
import com.example.docxddd.application.folder.strategy.createdocument.CreateDocumentStrategyFactory;
import com.example.docxddd.application.folder.strategy.createdocument.CreateDocumentStrategyInput;
import com.example.docxddd.application.folder.strategy.updatedocument.UpdateDocumentStrategyFactory;
import com.example.docxddd.domain.common.Result;
import com.example.docxddd.domain.folder.entity.Folder;
import com.example.docxddd.domain.folder.repository.FolderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

// Não usei a classe Result aqui pelas questões de como transações funcionam com Spring/Hibernate
// Para que o rollback seja feito, o método deve lançar uma runtimeexception
@RequiredArgsConstructor
@Service
public class FolderApplicationService extends TransactionalApplicationService {

    private final FolderRepository folderRepository;

//    private final DocumentCopyApplicationService documentCopyApplicationService;

    private final CreateDocumentStrategyFactory createDocumentStrategyFactory;

    private final UpdateDocumentStrategyFactory updateDocumentStrategyFactory;

    @Transactional
    public FolderDto create(CreateFolderDto dto) {

        var folderCreateResult = Folder.create(
                dto.getFolderType(),
                dto.getExternalId());

        if (folderCreateResult.isError()) {
            throw new IllegalArgumentException(folderCreateResult.getError());
        }

        var folder = folderCreateResult.getValue();

        folderRepository.save(folder);

        return toFolderDto(folder);
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

//    public List<FolderDto> listAll() {
//        var folders = folderRepository.findAll();
//
//        return folders.stream()
//                .map(this::toFolderDto)
//                .collect(Collectors.toList());
//    }

    public Optional<FolderDto> findById(Long folderId) {
        var folderOptional = folderRepository.findById(folderId);

        return folderOptional.map(this::toFolderDto);
    }

    // TODO - YAGNI - Transformar esse AppService em um Controller pois o único uso dele é via REST API
    @Transactional
    public Result<DocumentDto> createDocument(Long folderId,
                                              CreateDocumentDto dto) {

        var folderOptional = folderRepository.findById(folderId);
        if (folderOptional.isEmpty()) {
            return rollbackResult(Result.error("Folder not exists"));
        }
        var folder = folderOptional.get();

        var createDocumentStrategyOptional = createDocumentStrategyFactory
                .findCreateStrategyByDocumentType(dto.getDocumentType());
        if (createDocumentStrategyOptional.isEmpty()) {
            return rollbackResult(Result.error("Document Type not exists"));
        }

        var createDocumentResult = createDocumentStrategyOptional.get().create(
                new CreateDocumentStrategyInput(folder.getId(), dto));
        if (createDocumentResult.isError()) {
            return rollbackResult(Result.error(createDocumentResult.getError()));
        }

        var document = createDocumentResult.getValue();
        var addDocumentResult = folder.addDocument(document);
        if (addDocumentResult.isError()) {
            return rollbackResult(Result.error(addDocumentResult.getError()));
        }
        if (!addDocumentResult.getValue()) {
            return rollbackResult(Result.error("Document with the same content already exists"));
        }

        return Result.ok(new DocumentDto(document));

//        var folder = folderRepository.findById(folderId)
//                .orElseThrow(() -> new NoSuchElementException("Folder not exists"));
//
//        var createDocumentStrategy = createDocumentStrategyFactory
//                .findCreateStrategyByDocumentType(dto.getDocumentType())
//                .orElseThrow(() -> new NoSuchElementException("Document Type not exists"));
//
//        var createDocumentResult = createDocumentStrategy.create(
//                new CreateDocumentStrategyInput(folder.getId(), dto));
//        if (createDocumentResult.isError()) {
//            throw new IllegalArgumentException(createDocumentResult.getError());
//        }
//
//        var document = createDocumentResult.getValue();
//        var addDocumentResult = folder.addDocument(document);
//
//        if (addDocumentResult.isError()) {
//            throw new IllegalArgumentException(addDocumentResult.getError());
//        }
//
//        if (!addDocumentResult.getValue()) {
//            throw new IllegalStateException("Document with the same content already exists");
//        }
//
//        return new DocumentDto(document);
    }

    @Transactional
    public DocumentDto updateDocument(Long folderId,
                                      Long documentId,
                                      UpdateDocumentDto dto) {

        var folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Folder not exists"));

        var document = folder.findDocumentById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document not exists"));

        var updateStrategyOptional = updateDocumentStrategyFactory
                .findUpdateStrategyByDocumentType(document.getDocumentType());
        if (updateStrategyOptional.isEmpty()) {
            throw new IllegalArgumentException("Document Type not exists");
        }

        var updateDocumentStrategy = updateStrategyOptional.get();

//        var updateDocumentResult =
//                updateDocumentStrategy.update(new UpdateDocumentStrategyInput(dto, document));
//        if (updateDocumentResult.isError()) {
//            return Result.error(updateDocumentResult.getError());
//        }
//
//        return Result.ok(new DocumentDto(document));
        return null;
    }

//        var folderOptional = folderRepository.findById(folderId);
//        if (folderOptional.isEmpty()) {
//            return Result.error("Folder not exists");
//        }
//        var folder = folderOptional.get();
//
//        var documentOptional = folder.findDocumentById(documentId);
//        if (documentOptional.isEmpty()) {
//            return Result.error("Document not exists");
//        }
//        var document = documentOptional.get();
//
//        var updateStrategyOptional = updateDocumentStrategyFactory
//                .findUpdateStrategyByDocumentType(document.getDocumentType());
//        if (updateStrategyOptional.isEmpty()) {
//            return Result.error("Document Type not exists");
//        }
//        var updateDocumentStrategy = updateStrategyOptional.get();
//
//        var updateDocumentResult =
//                updateDocumentStrategy.update(new UpdateDocumentStrategyInput(dto, document));
//        if (updateDocumentResult.isError()) {
//            return Result.error(updateDocumentResult.getError());
//        }
//
//        return Result.ok(new DocumentDto(document));
//    }

    // TODO - Melhorar esse método, está desonesto ainda...
    @Transactional
    public Boolean removeDocumentById(Long folderId,
                                      Long documentId) {

        var folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("Folder not exists"));

        Result<Boolean> isRemovedResult = folder.removeDocumentById(documentId);
        if (isRemovedResult.isError()) {
            throw new IllegalArgumentException(isRemovedResult.getError());
        }

        return isRemovedResult.getValue();
    }

//    @PutMapping("/folders/{folderId}/document-copies")
//    public ResponseEntity<Envelope<DocumentCopyDto>> createDocumentCopy(@PathVariable Long folderId,
//                                                                        @RequestParam("file") MultipartFile file) {
//        var folderOptional = folderRepository.findById(folderId);
//        if (folderOptional.isEmpty()) {
//            return ResponseEntity.badRequest().body(Envelope.error("Folder not exists"));
//        }
//
//        var storeDocumentResult = documentCopyApplicationService
//                .storeDocumentCopy(folderId, file.getOriginalFilename());
//
//        if (storeDocumentResult.isError()) {
//            return ResponseEntity.badRequest().body(Envelope.error(storeDocumentResult.getError()));
//        }
//
//        return ResponseEntity.ok(Envelope.ok(new DocumentCopyDto(file.getOriginalFilename())));
//    }
}
