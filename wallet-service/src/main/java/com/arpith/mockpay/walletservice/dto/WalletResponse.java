package com.arpith.mockpay.walletservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletResponse {
    private String walletId;
    private Long balance;
    private String currency;
}
