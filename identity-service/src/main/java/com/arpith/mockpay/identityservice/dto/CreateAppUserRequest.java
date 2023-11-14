package com.arpith.mockpay.identityservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppUserRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    private String email;

    @Min(18)
    @NotNull
    private Integer age;

    @NotBlank
    private String mobile;

    @NotBlank
    private String password;

    @NotBlank
    private String userType;
}
