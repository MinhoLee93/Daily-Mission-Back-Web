package com.dailymission.api.springboot.web.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/*
* Let’s create the repository layer for accessing data from the database.
* The following UserRepository interface provides database functionalities for the User entity.
* Thanks to Spring-Data-JPA, we don’t need to write much code here -jh
* */
public interface UserRepository extends JpaRepository<User, Long> {

    // oauth2
    Optional<User> findByEmail(String email);

    // oauth2
    Boolean existsByEmail(String email);

}
