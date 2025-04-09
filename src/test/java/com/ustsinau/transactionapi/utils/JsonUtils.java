package com.ustsinau.transactionapi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readJsonFromFile(String path, Class<T> clazz)  {
        try {
            return objectMapper.readValue(new File(path), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readJsonFromFile(String path) {
        try {
            // Чтение содержимого файла как строки
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON from file: " + path, e);
        }
    }
}
