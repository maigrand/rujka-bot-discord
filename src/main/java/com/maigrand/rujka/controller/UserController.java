package com.maigrand.rujka.controller;

import com.maigrand.rujka.entity.UserEntity;
import com.maigrand.rujka.payload.user.*;
import com.maigrand.rujka.security.JwtTokenProvider;
import com.maigrand.rujka.service.UserService;
import com.maigrand.rujka.view.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "Пользователь")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "Получить текущего пользователя (по token в header)")
    public UserEntity currentUser(@AuthenticationPrincipal UserEntity userEntity) {
        return userEntity;
    }

    @PostMapping("/sign-in")
    @ApiOperation(value = "Авторизация", tags = "Авторизация")
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
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Регистрация")
    public ResponseEntity<UserView> register(@RequestBody UserDetails details) {
        UserEntity userEntity = this.userService.create(details);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserView(userEntity));
    }
}
