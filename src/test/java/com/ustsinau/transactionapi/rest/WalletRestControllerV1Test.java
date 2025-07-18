package com.ustsinau.transactionapi.rest;


import com.ustsinau.transactionapi.config.ContainersConfiguration;
import com.ustsinau.transactionapi.config.TestConfig;
import com.ustsinau.transactionapi.dto.request.WalletTypeCreateRequestDto;
import com.ustsinau.transactionapi.entity.WalletEntity;
import com.ustsinau.transactionapi.entity.WalletTypeEntity;
import com.ustsinau.transactionapi.enums.Status;
import com.ustsinau.transactionapi.repository.PaymentRequestRepository;
import com.ustsinau.transactionapi.repository.WalletRepository;
import com.ustsinau.transactionapi.repository.WalletTypeRepository;
import com.ustsinau.transactionapi.service.WalletTypeService;
import com.ustsinau.transactionapi.utils.JsonUtils;
import jakarta.persistence.EntityManager;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private WalletTypeRepository walletTypeRepository;
    @Autowired
    private WalletTypeService walletTypeService;
    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    private UUID walletTypeUid;


    @BeforeEach
 //   @Transactional
    public void setUp() {

        System.out.println("Count walletType before delete: " + walletTypeRepository.count()); // Проверка количества записей
        System.out.println("Count wallet before delete: " + walletRepository.count()); // Проверка количества записей
        System.out.println("Count payment before delete: " + paymentRequestRepository.count()); // Проверка количества записей

        paymentRequestRepository.deleteAll();
        walletRepository.deleteAll();
        walletTypeRepository.deleteAll();

        System.out.println("Count payment after delete: " + paymentRequestRepository.count());
        System.out.println("Count walletType after delete: " + walletTypeRepository.count());
        System.out.println("Count wallet after delete: " + walletRepository.count());

    }

    private void createTestWalletType() {
        WalletTypeCreateRequestDto request = JsonUtils.readJsonFromFile(
                "src/test/resources/json/create_WalletType.json",
                WalletTypeCreateRequestDto.class);
        WalletTypeEntity savedWalletType = walletTypeService.createWalletType(request);
        this.walletTypeUid = savedWalletType.getUid();
        //  entityManager.flush();
    }

    @Test
    @DisplayName("Test create wallet functionality 2")
    @SneakyThrows
    public void givenValidRequest_whenCreateWallet_thenWalletIsCreated2() {
        createTestWalletType();
        // Given
        String requestJson = """
                {
                    "name": "second",
                    "userUid": "123e4567-e89b-12d3-a456-426614174000",
                    "walletTypeUid": "%s"
                }
                """.formatted(walletTypeUid.toString());
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
    @DisplayName("Test create wallet functionality")
    @SneakyThrows
    public void givenValidRequest_whenCreateWallet_thenWalletIsCreated() {
        createTestWalletType();
        // Given
        String requestJson = """
                {
                    "name": "second",
                    "userUid": "123e4567-e89b-12d3-a456-426614174000",
                    "walletTypeUid": "%s"
                }
                """.formatted(walletTypeUid.toString());
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
        WalletTypeEntity mainType = walletTypeRepository.save(
                WalletTypeEntity.builder()
                        .name("Основной")
                        .currencyCode("RUB")
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .userType("Individual")
                        .build()
        );
        walletRepository.save(
                WalletEntity.builder()
                        .name("Чужой кошелек")
                        .createdAt(LocalDateTime.now())
                        .userUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                        .status(Status.ACTIVE)
                        .walletType(mainType)
                        .balance(new BigDecimal("200.00"))
                        .build()
        );
        // When
        mockMvc.perform(get("/api/v1/wallets/user/{user_uid}/currency/{currency}",
                        "123e4567-e89b-12d3-a456-426614174000",
                        "RUB")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Чужой кошелек")))
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
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID otherUserId = UUID.fromString("223e4567-e89b-12d3-a456-426614174002");

        // Создаем тестовые типы кошельков
        WalletTypeEntity mainType = walletTypeRepository.save(
                WalletTypeEntity.builder()
                        .name("Основной")
                        .currencyCode("RUB")
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .userType("Individual")
                        .build()
        );
        System.out.println("mainType: " + mainType);

        WalletTypeEntity secondaryType = walletTypeRepository.save(
                WalletTypeEntity.builder()
                        .name("Дополнительный")
                        .status(Status.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .currencyCode("USD")
                        .build()
        );

        // Создаем тестовые кошельки
        List<WalletEntity> testWallets = Arrays.asList(
                WalletEntity.builder()
                        .name("Основной кошелек")
                        .createdAt(LocalDateTime.now())
                        .userUid(userId)
                        .status(Status.ACTIVE)
                        .walletType(mainType)
                        .balance(new BigDecimal("1000.00"))
                        .build(),

                WalletEntity.builder()
                        .name("Долларовый кошелек")
                        .createdAt(LocalDateTime.now())
                        .userUid(userId)
                        .status(Status.ACTIVE)
                        .walletType(secondaryType)
                        .balance(new BigDecimal("500.00"))
                        .build(),

                // Кошелек другого пользователя (не должен быть в результате)
                WalletEntity.builder()
                        .name("Чужой кошелек")
                        .createdAt(LocalDateTime.now())
                        .userUid(otherUserId)
                        .status(Status.ACTIVE)
                        .walletType(mainType)
                        .balance(new BigDecimal("200.00"))
                        .build()
        );
        walletRepository.saveAll(testWallets);
        walletRepository.findByUserUid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        // When & Then
        mockMvc.perform(get("/api/v1/wallets/user/{user_uid}", userId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) // Ожидаем 2 кошелька для этого пользователя
                .andExpect(jsonPath("$[*].userUid", everyItem(is(userId.toString()))))
                .andExpect(jsonPath("$[0].name", is("Основной кошелек")))
                .andExpect(jsonPath("$[0].balance", is(1000.00)))
                // не walletType тк другое тело ответа WalletResponse
                .andExpect(jsonPath("$[0].walletTypeUid", is(mainType.getUid().toString())))
                .andExpect(jsonPath("$[1].name", is("Долларовый кошелек")))
                .andExpect(jsonPath("$[1].balance", is(500.00)))
          //      .andExpect(jsonPath("$[1].walletType.currencyCode", is("USD")))
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString();
                    System.out.println("Response: " + response);
                });
    }


}

