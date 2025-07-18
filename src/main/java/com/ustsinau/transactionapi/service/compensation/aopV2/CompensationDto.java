package com.ustsinau.transactionapi.service.compensation.aopV2;

import lombok.NonNull;

public record CompensationDto(Runnable runnable, boolean compensate, @NonNull String stepName) {
}
