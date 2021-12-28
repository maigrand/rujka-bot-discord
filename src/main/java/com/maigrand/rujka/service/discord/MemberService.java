package com.maigrand.rujka.service.discord;

import com.maigrand.rujka.entity.discord.MemberEntity;
import com.maigrand.rujka.repository.discord.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberEntity> findAll() {
        return memberRepository.findAll();
    }

    public MemberEntity findByMemberIdAndGuildId(String memberId, String guildId) {
        return memberRepository.findByMemberIdAndGuildId(memberId, guildId)
                .orElse(null);
    }

    public MemberEntity save(MemberEntity entity) {
        return memberRepository.save(entity);
    }

    public List<MemberEntity> findAllByGuildId(String guildId) {
        return memberRepository.findAllByGuildId(guildId);
    }
}
