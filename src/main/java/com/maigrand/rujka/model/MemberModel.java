package com.maigrand.rujka.model;

import com.maigrand.rujka.entity.discord.RoleEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberModel {

    public String guildId;

    public String guildName;

    public String memberId;

    public String name;

    public String discordTag;

    public String avatarUrl;

    public String color;

    public RoleEntity role;
}
