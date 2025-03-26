package com.ustsinau.transactionapi.entity;

import lombok.Data;


@Data
public class MigrationSource {
    private String url;
    private String username;
    private String password;
}
