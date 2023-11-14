package com.arpith.mockpay.identityservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponse {
    private String name;

    private String email;

    private Integer age;

    private String mobile;
}
