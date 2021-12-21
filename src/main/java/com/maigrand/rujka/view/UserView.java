package com.maigrand.rujka.view;

import com.maigrand.rujka.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserView {

    private final Integer id;

    private final String email;

    //todo: библиотека mapstruct
    public UserView(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.email = userEntity.getEmail();
    }
}