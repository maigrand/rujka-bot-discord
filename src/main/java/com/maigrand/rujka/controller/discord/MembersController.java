package com.maigrand.rujka.controller.discord;

import com.maigrand.rujka.model.MemberModel;
import com.maigrand.rujka.payload.PaginationDetails;
import com.maigrand.rujka.service.discord.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@Api(tags = "Мемберы дискорда")
@RequiredArgsConstructor
public class MembersController {

    private final MemberService memberService;

    @GetMapping
    @ApiOperation(value = "Получить список всех мемберов")
    public PaginationDetails<MemberModel> list(Pageable pageable) {
        return memberService.findAll(pageable);
    }

    @GetMapping("/{discordId}")
    @ApiOperation(value = "Получить мембера по discord ID")
    public MemberModel find(@PathVariable("discordId") String discordId) {
        return memberService.findByDiscordId(discordId);
    }
}
