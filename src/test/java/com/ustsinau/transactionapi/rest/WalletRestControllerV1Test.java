package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.config.ContainersConfiguration;
import com.ustsinau.transactionapi.config.TestConfig;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.repository.PaymentRequestRepository;
import com.ustsinau.transactionapi.repository.WalletRepository;
import com.ustsinau.transactionapi.repository.WalletTypeRepository;
import com.ustsinau.transactionapi.service.WalletService;
import com.ustsinau.transactionapi.service.WalletTypeService;
import com.ustsinau.transactionapi.utils.DataWalletTypeUtils;
import com.ustsinau.transactionapi.utils.DataWalletUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import({ContainersConfiguration.class, TestConfig.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletTypeRepository walletTypeRepository;
    @Autowired
    private WalletTypeService walletTypeService;
    @Autowired
    private PaymentRequestRepository paymentRequestRepository;


    @BeforeEach
    public void setUp() {
        paymentRequestRepository.deleteAll();
        walletRepository.deleteAll();
        walletTypeRepository.deleteAll();
    }

    @Test
    @DisplayName("Test update wallet's name - success.")
    @SneakyThrows
    public void givenValidId_whenGetWalletById_thenSuccess() {
        //Given
        WalletTypeEntity walletType = walletTypeService.createWalletType(DataWalletTypeUtils.getWalletTypeFirstCreateRequestDtoTransient());
        WalletEntity walletEntity = walletService.createWallet(DataWalletUtils.getWalletFirstCreateRequestDtoTransient(walletType.getUid()));
        // When
        mockMvc.perform(put("/api/v1/wallets/user/{wallet_uid}/update/{name}",
                        walletEntity.getUid(),
                        "New Name")
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uid", is(walletEntity.getUid().toString())))
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.userUid", is("123e4567-e89b-12d3-a456-426614174000")))
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    System.out.println("Response: " + response);
                });
    }

    @Test
    @DisplayName("Test create wallet functionality - success")
    @SneakyThrows
    public void givenValidRequest_whenCreateWallet_thenWalletIsCreated() {
        //Given
        WalletTypeEntity walletType = walletTypeService.createWalletType(DataWalletTypeUtils.getWalletTypeFirstCreateRequestDtoTransient());

        String requestJson = """
                {
                    "name": "second",
                    "userUid": "123e4567-e89b-12d3-a456-426614174000",
                    "walletTypeUid": "%s"
                }
                """.formatted(walletType.getUid());
        // When
        mockMvc.perform(post("/api/v1/wallets/user/create-wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("second")))
                .andExpect(jsonPath("$.userUid", is("123e4567-e89b-12d3-a456-426614174000")))
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    System.out.println("Response: " + response);
                });
    }

    @Test
    @DisplayName("Test get wallet by UserId And Currency - success")
    @SneakyThrows
    public void givenUserIdAndCurrency_whenGetWallet_ReturnSuccess() {
        WalletTypeEntity walletType = walletTypeService.createWalletType(DataWalletTypeUtils.getWalletTypeFirstCreateRequestDtoTransient());
        walletService.createWallet(DataWalletUtils.getWalletFirstCreateRequestDtoTransient(walletType.getUid()));
        // When
        mockMvc.perform(get("/api/v1/wallets/user/{user_uid}/currency/{currency}",
                        "123e4567-e89b-12d3-a456-426614174000",
                        "RUB")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("First wallet")))
                .andExpect(jsonPath("$.userUid", is("123e4567-e89b-12d3-a456-426614174000")))
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    System.out.println("Response: " + response);
                });
    }

    @Test
    @DisplayName("Test get user's wallets by UserId - should return all user wallets")
    @SneakyThrows
    public void givenUserId_whenGetWallets_thenReturnUserWallets() {
        // Given
        WalletTypeEntity walletTypeFirst = walletTypeService.createWalletType(DataWalletTypeUtils.getWalletTypeFirstCreateRequestDtoTransient());
        WalletTypeEntity walletTypeSecond = walletTypeService.createWalletType(DataWalletTypeUtils.getWalletTypeSecondCreateRequestDtoTransient());
        WalletEntity walletEntityFirst = walletService.createWallet(DataWalletUtils.getWalletFirstCreateRequestDtoTransient(walletTypeFirst.getUid()));
        WalletEntity walletEntitySecond = walletService.createWallet(DataWalletUtils.getWalletSecondCreateRequestDtoTransient(walletTypeSecond.getUid()));
        WalletEntity walletEntityOtherPerson = walletService.createWallet(DataWalletUtils.getWalletFirstCreateRequestDtoSecondPersonTransient(walletTypeFirst.getUid()));

        List<WalletEntity> testWallets = Arrays.asList(walletEntityFirst, walletEntitySecond, walletEntityOtherPerson);
        walletRepository.saveAll(testWallets);
        walletRepository.findByUserUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        // When & Then
        mockMvc.perform(get("/api/v1/wallets/user/{user_uid}", walletEntityFirst.getUserUid())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) // Ожидаем 2 кошелька для этого пользователя
                .andExpect(jsonPath("$[*].userUid", everyItem(is(walletEntityFirst.getUserUid().toString()))))
                .andExpect(jsonPath("$[0].name", is("First wallet")))
                //  .andExpect(jsonPath("$[0].balance", is(1000.00)))
                // не walletType тк другое тело ответа WalletResponse
                .andExpect(jsonPath("$[0].walletTypeUid", is(walletTypeFirst.getUid().toString())))
                .andExpect(jsonPath("$[1].name", is("Second wallet")))
                //    .andExpect(jsonPath("$[1].balance", is(500.00)))
                //      .andExpect(jsonPath("$[1].walletType.currencyCode", is("USD")))
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    System.out.println("Response: " + response);
                });
    }

}

