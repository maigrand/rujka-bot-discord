package com.maigrand.rujka.controller;

import com.maigrand.rujka.entity.UserEntity;
import com.maigrand.rujka.payload.user.*;
import com.maigrand.rujka.security.JwtTokenProvider;
import com.maigrand.rujka.service.UserService;
import com.maigrand.rujka.view.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserService userService;

    @GetMapping
    public UserEntity currentUser(@AuthenticationPrincipal UserEntity userEntity) {
        return userEntity;
    }

    @PostMapping("/sign-in")
    public AuthenticationTokenDetails authenticate(@RequestBody @Valid AuthenticationCredentials authenticationCredentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationCredentials.getEmail(),
                        authenticationCredentials.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication, true);
        return new AuthenticationTokenDetails(token);
    }

    @PostMapping("/sign-up")
    public UserView register(@RequestBody UserDetails details) {
        UserEntity userEntity = this.userService.create(details);
        return new UserView(userEntity);
    }
}
