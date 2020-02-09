package com.dailymission.api.springboot.web.account;

import com.dailymission.api.springboot.web.repository.account.Account;
import lombok.Builder;

public class AccountSetup {

    private String name;

    @Builder
    public AccountSetup(String name){
        this.name = name;
    }

    public Account build(){
        return buildAccount();
    }

    private Account buildAccount() {
        return Account.builder()
                .email("test")
                .name(name)
                .picture("test")
                .build();
    }
}
