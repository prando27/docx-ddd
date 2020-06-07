package com.example.docxddd.domain.user;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    Optional<User> findById(Long id);
}
