package com.example.docxddd.domain.user;

import com.example.docxddd.domain.common.AggregateRoot;
import com.example.docxddd.domain.folder.entity.Folder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

// TODO Conceito de User que gerencia as suas folders
// TODO Testar sobre redefinir o ID sem GeneratedValue (A ideia é usar o Id do usuário na main)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_table")
public class User extends AggregateRoot {

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Folder> folders;

    public void addFolder(Folder folder) {
    }

//    public Optional<Folder> getUserFolder() {
//        return folders.stream()
//                .filter(folder -> FolderType.USER.equals(folder.getFolderType()))
//                .findFirst();
//    }
}
