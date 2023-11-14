package com.arpith.mockpay.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTransactionResponse {
    private String transactionId;
    private String receiverId;
    private String senderId;
    private Long amount;
    private String status;
    private String reason;
}
