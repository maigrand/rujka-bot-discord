package com.maigrand.rujka.service;

import com.maigrand.rujka.entity.UserEntity;
import com.maigrand.rujka.payload.user.UserDetails;
import com.maigrand.rujka.repository.UserRepository;
import com.maigrand.rujka.validator.group.OnCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;

@Service
@Validated
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    @Validated(OnCreate.class)
    public UserEntity create(String email, String password, Boolean active) {
        if (this.userRepository.existsByEmail(email)) {
            throw new EntityExistsException("user exists");
        }
        UserEntity entity = new UserEntity();

        entity.setEmail(email);
        entity.setPassword(this.passwordEncoder.encode(password));
        entity.setActive(active);

        return this.userRepository.save(entity);
    }

    @Validated(OnCreate.class)
    public UserEntity create(UserDetails details) {
        return create(details.getEmail(), details.getPassword(), true);
    }
}
