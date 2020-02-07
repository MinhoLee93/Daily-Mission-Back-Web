package com.dailymission.api.springboot.web.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> , AccountRepositoryCustom {

    Optional<Account> findByEmail(String email);

    Account save(Account account);
}
