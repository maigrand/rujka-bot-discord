package com.maigrand.rujka.service.discord;

import com.maigrand.rujka.entity.discord.MemberEntity;
import com.maigrand.rujka.model.MemberModel;
import com.maigrand.rujka.payload.PaginationDetails;
import com.maigrand.rujka.repository.discord.MemberRepository;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.util.PageUtil;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class MemberService implements PageUtil {

    private final JdaService jdaService;

    private final MemberRepository memberRepository;

    //todo: Ознакомиться с MemberCachePolicy, брать мемберов из кеша
    @Deprecated
    public PaginationDetails<MemberModel> findAll(Pageable pageable) {
        JDA jda = jdaService.getJda();
        List<MemberModel> list = new ArrayList<>();
        for (Guild guild : jda.getGuilds()) {
            for (Member member : guild.getMembers()) {
                MemberModel memberModel = discordMemberToMemberModel(guild, member);
                list.add(memberModel);
            }
        }

        Page<MemberModel> page = (Page<MemberModel>) toPage(list, pageable);
        return new PaginationDetails<>(page);
    }

    public PaginationDetails<MemberModel> findAll(String guildId, Pageable pageable) {
        JDA jda = this.jdaService.getJda();
        //List<MemberModel> list = new ArrayList<>();
        //todo: if guild not found throw exception
        Guild guild = jda.getGuildById(guildId);
        List<MemberModel> list = guild.getMembers().stream()
                .map(member -> discordMemberToMemberModel(guild, member))
                .collect(Collectors.toList());
        Page<MemberModel> page = (Page<MemberModel>) toPage(list, pageable);
        return new PaginationDetails<>(page);
    }

    public MemberModel findDiscordMemberByGuildIdAndMemberId(String guildId, String memberId) {
        JDA jda = this.jdaService.getJda();
        //todo: if guild not found
        Guild guild = jda.getGuildById(guildId);
        //todo: if member not found
        Member member = guild.getMemberById(memberId);
        return discordMemberToMemberModel(guild, member);
    }

    @Deprecated
    public MemberModel findByDiscordId(String discordId) {
        for (Guild guild : this.jdaService.getJda().getGuilds()) {
            for (Member member : guild.getMembers()) {
                if (member.getId().equals(discordId)) {
                    return discordMemberToMemberModel(guild, member);
                }
            }
        }
        return null;
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

    private MemberModel discordMemberToMemberModel(Guild guild, Member member) {
        MemberModel memberModel = new MemberModel();
        memberModel.setGuildId(guild.getId());
        memberModel.setGuildName(guild.getName());
        memberModel.setName(member.getEffectiveName());
        memberModel.setMemberId(member.getId());
        memberModel.setDiscordTag(member.getUser().getAsTag());
        memberModel.setAvatarUrl(member.getUser().getEffectiveAvatarUrl());
        memberModel.setColor(member.getColor() == null ?
                null : "#" + Integer.toHexString(member.getColor().getRGB()).substring(2));

        return memberModel;
    }
}
