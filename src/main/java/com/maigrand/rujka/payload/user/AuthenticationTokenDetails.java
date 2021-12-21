package com.maigrand.rujka.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationTokenDetails {

    private final String token;
    private final String tokenType = "Bearer";
}
