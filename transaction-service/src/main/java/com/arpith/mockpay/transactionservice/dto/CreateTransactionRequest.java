package com.arpith.mockpay.transactionservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransactionRequest {
    @NotBlank
    private String receiverId;

    @Min(1)
    private Double amount;

    private String reason;
}
