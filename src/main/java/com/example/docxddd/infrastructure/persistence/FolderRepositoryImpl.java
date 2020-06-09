package com.example.docxddd.infrastructure.persistence;

import com.example.docxddd.domain.folder.entity.Folder;
import com.example.docxddd.domain.folder.repository.FolderRepository;

import lombok.AllArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Repository
public class FolderRepositoryImpl implements FolderRepository {

    private final FolderRepositoryJpa folderRepositoryJpa;

    @Override
    public void save(Folder folder) {
        folderRepositoryJpa.save(folder);
    }

    @Override
    public Optional<Folder> findById(Long id) {
        return folderRepositoryJpa.findByIdLeftJoinFetchDocuments(id);
    }
}

interface FolderRepositoryJpa extends JpaRepository<Folder, Long> {

    @Query("SELECT f " +
            "FROM Folder f " +
            "LEFT JOIN FETCH f.documents " +
            "WHERE f.id = :id")
    Optional<Folder> findByIdLeftJoinFetchDocuments(@Param("id") Long id);
}
