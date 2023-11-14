package com.arpith.mockpay.identityservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecuredUserResponse {
    private String username;

    private String authorities;

    private Boolean isAccountNonExpired;

    private Boolean isAccountNonLocked;

    private Boolean isCredentialsNonExpired;

    private Boolean isEnabled;
}
