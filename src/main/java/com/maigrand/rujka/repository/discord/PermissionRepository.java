package com.maigrand.rujka.repository.discord;

import com.maigrand.rujka.entity.discord.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {

    Optional<PermissionEntity> findByName(String name);
}
