package com.maigrand.rujka.controller.discord;

import com.maigrand.rujka.model.GuildModel;
import com.maigrand.rujka.model.MemberModel;
import com.maigrand.rujka.payload.PaginationDetails;
import com.maigrand.rujka.service.discord.GuildService;
import com.maigrand.rujka.service.discord.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/guilds")
@Api(tags = "Гильдии")
@RequiredArgsConstructor
public class GuildsController {

    private final GuildService guildService;

    private final MemberService memberService;

    @GetMapping
    @ApiOperation(value = "Получить список всех гильдий")
    public PaginationDetails<GuildModel> list(Pageable pageable) {
        return guildService.findAll(pageable);
    }

    @GetMapping("/{guildId}")
    @ApiOperation(value = "Получить гильдию по guildId")
    public GuildModel show(@PathVariable("guildId") String guildId) {
        return guildService.findByDiscordId(guildId);
    }

    @GetMapping("/{guildId}/members")
    @ApiOperation(value = "Получить список дискорд мемберов по guildId")
    public PaginationDetails<MemberModel> memberList(@PathVariable("guildId") String guildId,
            Pageable pageable) {
        return memberService.findAll(guildId, pageable);
    }

    @GetMapping("/{guildId}/member/{memberId}")
    @ApiOperation(value = "Получить дискорд мембера по guildId и memberId")
    public MemberModel member(@PathVariable("guildId") String guildId,
            @PathVariable("memberId") String memberId) {
        return memberService.findDiscordMemberByGuildIdAndMemberId(guildId, memberId);
    }
}
