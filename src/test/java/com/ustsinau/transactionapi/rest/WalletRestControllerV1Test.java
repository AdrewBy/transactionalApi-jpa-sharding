package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.config.ContainersConfiguration;
import com.ustsinau.transactionapi.dto.request.WalletTypeCreateRequestDto;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.enums.Status;
import com.ustsinau.transactionapi.mappers.WalletMapper;
import com.ustsinau.transactionapi.repository.WalletRepository;
import com.ustsinau.transactionapi.repository.WalletTypeRepository;
import com.ustsinau.transactionapi.service.WalletService;
import com.ustsinau.transactionapi.service.WalletTypeService;
import com.ustsinau.transactionapi.utils.JsonUtils;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(ContainersConfiguration.class)
@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTypeRepository walletTypeRepository;

    @Autowired
    private WalletTypeService walletTypeService;

    @Autowired
    private WalletMapper walletMapper;


    @BeforeEach
    public void setUp() {
//        walletRepository.deleteAll();
//        walletTypeRepository.deleteAll();

        WalletTypeEntity walletTypeEntity =
                walletTypeService.createWalletType(WalletTypeCreateRequestDto
                        .builder()
                        .name("Основной кошелёк")
                        .currencyCode("RUB")
                        .userType("INDIVIDUAL")
                        .creator("admin@example.com")
                        .build());

    }


    @SneakyThrows
    @Test
    void testCreate_Wallet_Success() {

        mockMvc.perform(post("/user/create-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.readJsonFromFile("src/test/resources/json/create_wallet.json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("first")))
                .andExpect(jsonPath("$.userUid", is("123e4567-e89b-12d3-a456-426614174000")));
    }
}
