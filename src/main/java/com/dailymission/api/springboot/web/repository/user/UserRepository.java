package com.dailymission.api.springboot.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , UserRepositoryCustom {

    Optional<User> findByEmail(String email);
}
