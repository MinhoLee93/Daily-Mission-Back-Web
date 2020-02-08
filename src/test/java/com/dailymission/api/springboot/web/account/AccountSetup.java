package com.dailymission.api.springboot.web.account;

import com.dailymission.api.springboot.web.repository.account.Account;
import lombok.Builder;

public class AccountSetup {

    @Builder
    public AccountSetup(){

    }

    public Account build(){
        return buildAccount();
    }

    private Account buildAccount() {
        return Account.builder()
                .email("test")
                .name("test")
                .picture("test")
                .build();
    }
}
