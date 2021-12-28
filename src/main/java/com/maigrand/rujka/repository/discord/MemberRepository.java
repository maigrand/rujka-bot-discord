package com.maigrand.rujka.repository.discord;

import com.maigrand.rujka.entity.discord.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    Optional<MemberEntity> findByMemberIdAndGuildId(String memberId, String guildId);

    List<MemberEntity> findAllByGuildId(String guildId);
}
