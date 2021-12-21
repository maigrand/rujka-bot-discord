package com.maigrand.rujka.payload.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthenticationCredentials {

    @Email(message = "{email.valid_email}")
    @NotBlank(message = "{email.not_blank}")
    private String email;

    @NotBlank(message = "{password.not_blank}")
    private String password;
}
