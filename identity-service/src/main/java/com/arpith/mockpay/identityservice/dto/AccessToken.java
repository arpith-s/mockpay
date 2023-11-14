package com.arpith.mockpay.identityservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    String username;

    String token;
}