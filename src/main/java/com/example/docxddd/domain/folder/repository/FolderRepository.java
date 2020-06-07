package com.example.docxddd.domain.folder.repository;

import com.example.docxddd.domain.folder.entity.Folder;

import java.util.Optional;

public interface FolderRepository {

    void save(Folder folder);

    Optional<Folder> findById(Long id);
}
