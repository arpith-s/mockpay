package com.arpith.mockpay.identityservice.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppUserRequest {
    @Size(max = 100)
    private String name;

    private String email;

    private Integer age;

    private String mobile;

    private String password;

    private String authorities;

    private Boolean isAccountNonExpired;

    private Boolean isAccountNonLocked;

    private Boolean isCredentialsNonExpired;

    private Boolean isEnabled;
}
