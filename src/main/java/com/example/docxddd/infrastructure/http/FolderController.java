package com.example.docxddd.infrastructure.http;

import com.example.docxddd.application.FolderApplicationService;
import com.example.docxddd.application.dto.CreateFolderDto;
import com.example.docxddd.application.dto.FolderDto;
import com.example.docxddd.infrastructure.http.dto.Envelope;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class FolderController {

    private final FolderApplicationService folderApplicationService;

    @PostMapping("/folders")
    public ResponseEntity<Envelope<FolderDto>> create(@RequestBody CreateFolderDto dto) {

        var folderDto = folderApplicationService.create(dto);

        return ResponseEntity.ok(Envelope.ok(folderDto));
    }

//    @GetMapping("/folders")
//    public ResponseEntity<Envelope<List<FolderDto>>> listAll() {
//        return ResponseEntity.ok(
//                Envelope.ok(folderApplicationService.listAll()));
//    }

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<Envelope<FolderDto>> findById(@PathVariable Long folderId) {

        Optional<FolderDto> folderDtoOptional = folderApplicationService.findById(folderId);
        if (folderDtoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Envelope.ok(folderDtoOptional.get()));
    }

//    @PutMapping("/folders/{folderId}/documents")
//    public ResponseEntity<Envelope<DocumentDto>> createDocument(@PathVariable Long folderId,
//                                                                @RequestBody CreateDocumentDto dto,
//                                                                HttpServletRequest request) {
//        // Ideia: Na autenticação, colocar a folder na request e enviar no método createDocument
//        // no lugar do folderId
//        try {
//            var documentDto = folderApplicationService.createDocument(folderId, dto);
//
//            return ResponseEntity.ok(Envelope.ok(documentDto));
//        } catch (NoSuchElementException ex) {
//            return ResponseEntity.notFound().build();
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.badRequest().body(Envelope.error(ex.getMessage()));
//        } catch (IllegalStateException ex) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(Envelope.error(ex.getMessage()));
//        }
//    }

//    @PutMapping("/folders/{folderId}/documents/{documentId}")
//    public ResponseEntity<Envelope<DocumentDto>> updateDocument(@PathVariable Long folderId,
//                                                                @PathVariable Long documentId,
//                                                                @RequestBody UpdateDocumentDto dto) {
//        Result<DocumentDto> updateDocumentResult = folderApplicationService
//                .updateDocument(folderId, documentId, dto);
//
//        if (updateDocumentResult.isError()) {
//            return ResponseEntity.badRequest().body(Envelope.error(updateDocumentResult.getError()));
//        }
//
//        return ResponseEntity.ok(Envelope.ok(updateDocumentResult.getValue()));
//    }

    @DeleteMapping("/folders/{folderId}/documents/{documentId}")
    public ResponseEntity<Envelope<Void>> removeDocumentById(@PathVariable Long folderId,
                                                   @PathVariable Long documentId) {

        try {
            Boolean isRemoved = folderApplicationService.removeDocumentById(folderId, documentId);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Envelope.error(ex.getMessage()));
        }
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
