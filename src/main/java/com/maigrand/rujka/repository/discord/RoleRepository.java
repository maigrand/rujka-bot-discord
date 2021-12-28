package com.maigrand.rujka.repository.discord;

import com.maigrand.rujka.entity.discord.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByRoleIdAndGuildId(String roleId, String guildId);

    List<RoleEntity> findAllByGuildId(String guildId);
}
