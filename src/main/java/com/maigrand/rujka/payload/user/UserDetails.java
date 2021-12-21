package com.maigrand.rujka.payload.user;

import com.maigrand.rujka.validator.group.OnCreate;
import com.maigrand.rujka.validator.group.OnUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@Data
public class UserDetails {

    @Email(message = "{email.valid}", groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "{email.not_blank}", groups = OnCreate.class)
    protected String email;

    @Size(min = 6, message = "{password.size}", groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "{password.not_blank}", groups = OnCreate.class)
    protected String password;

    protected Boolean active;
}
