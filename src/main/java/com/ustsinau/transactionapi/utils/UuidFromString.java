package com.ustsinau.transactionapi.utils;


import com.ustsinau.transactionapi.exception.UuidInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class UuidFromString {

    public UUID getUuidFromString(String uid) {
        UUID uuid;
        try {
            uuid = UUID.fromString(uid);
            log.info("UUID = " + uuid);
        } catch (IllegalArgumentException e) {
            throw new UuidInvalidException("Invalid UUID format: " + uid, "INVALID_UUID_FORMAT");
        }
        return uuid;
    }
}
