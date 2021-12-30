package com.maigrand.rujka.discord.permissionmodule;

import com.maigrand.rujka.discord.DiscordModule;
import com.maigrand.rujka.discord.util.DiscordId;
import com.maigrand.rujka.discord.util.InfoEmbedUtil;
import com.maigrand.rujka.entity.discord.*;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PermissionModule extends DiscordModule {

    private final PermissionService permissionService;

    private final MemberService memberService;

    private final RoleService roleService;

    public PermissionModule(JdaService jdaService,
            PermissionService permissionService,
            MemberService memberService,
            RoleService roleService) {
        super(jdaService);
        this.permissionService = permissionService;
        this.memberService = memberService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void init() {
        registerPermission(new String[]{
                "PERMISSION_MANAGER"
        });
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        if (event.getMember() == null) {
            return;
        }

        if (event.getAuthor().isBot()) {
            return;
        }

        if (!event.getMessage().getContentRaw().startsWith("perm")) {
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equals("perm/help")) {
            permissionInfo(event);
            return;
        }

        if (args[0].equals("perm/list")) {
            permissionList(event);
            return;
        }

        if (args[0].equals("perm/members")) {
            permissionMembers(event);
            return;
        }

        if (args[0].equals("perm/roles")) {
            permissionRoles(event);
            return;
        }

        if (args[0].equals("perm/add")) {
            permissionAdd(event);
            return;
        }

        if (args[0].equals("perm/remove")) {
            permissionRemove(event);
            return;
        }
    }

    private void permissionInfo(MessageReceivedEvent event) {
        String s = "Основные команды:" +
                "\n" +
                "`perm/list`" +
                "\n" +
                "`perm/members`" +
                "\n" +
                "`perm/roles`" +
                "\n" +
                "`perm/add`" +
                "\n" +
                "`perm/remove`";
        EmbedBuilder emb = InfoEmbedUtil.getEmbedBuilder(event.getJDA(), "Permission Module", s);
        event.getChannel().sendMessageEmbeds(emb.build()).queue();
    }

    private void permissionList(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "PERMISSION_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("```").append("\n");
        for (PermissionEntity permissionEntity : permissionService.findAll()) {
            sb.append(permissionEntity.getName()).append("\n");
        }
        sb.append("```");
        event.getChannel().sendMessage(sb).queue();
    }

    private void permissionMembers(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "PERMISSION_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }

        List<MemberEntity> memberEntityList = memberService.findAllByGuildId(event.getGuild().getId());
        if (memberEntityList.isEmpty()) {
            event.getChannel().sendMessage("Empty.").queue();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("```").append("\n");
        for (MemberEntity memberEntity : memberEntityList) {
            String effectiveName = event.getGuild().getMemberById(memberEntity.getMemberId()).getEffectiveName();
            sb.append(effectiveName).append(" permissions:").append("\n");
            for (PermissionEntity permissionEntity : memberEntity.getPermissionEntitySet()) {
                sb.append("- ").append(permissionEntity.getName()).append("\n");
            }
        }
        sb.append("```");
        event.getChannel().sendMessage(sb).queue();
    }

    private void permissionRoles(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "PERMISSION_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }

        List<RoleEntity> roleEntityList = roleService.findAllByGuildId(event.getGuild().getId());
        if (roleEntityList.isEmpty()) {
            event.getChannel().sendMessage("Empty.").queue();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("```").append("\n");
        for (RoleEntity roleEntity : roleEntityList) {
            String name = event.getGuild().getRoleById(roleEntity.getRoleId()).getName();
            sb.append(name).append(" permissions:").append("\n");
            for (PermissionEntity permissionEntity : roleEntity.getPermissionEntitySet()) {
                sb.append("- ").append(permissionEntity.getName()).append("\n");
            }
        }
        sb.append("```");
        event.getChannel().sendMessage(sb).queue();
    }

    private void permissionAdd(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "PERMISSION_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args.length != 3) {
            event.getChannel().sendMessage("Usage: cmd <@member or @role> <permName>").queue();
            return;
        }

        PermissionEntity permissionEntity = permissionService.findByName(args[2]);
        if (permissionEntity == null) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }

        Guild guild = event.getGuild();
        Member member = DiscordId.getMember(guild, args[1]);
        Role role = DiscordId.getRole(guild, args[1]);
        if (member != null) {
            MemberEntity memberEntity = memberService.findByMemberIdAndGuildId(member.getId(), guild.getId());
            if (memberEntity == null) {
                memberEntity = new MemberEntity();
                memberEntity.setMemberId(member.getId());
                memberEntity.setGuildId(guild.getId());
                memberEntity = memberService.save(memberEntity);
            }
            memberEntity.addPermission(permissionEntity);
            memberService.save(memberEntity);
            event.getChannel().sendMessage("Added.").queue();
            return;
        }

        if (role != null) {
            RoleEntity roleEntity = roleService.findByRoleIdAndGuildId(role.getId(), guild.getId());
            if (roleEntity == null) {
                roleEntity = new RoleEntity();
                roleEntity.setRoleId(role.getId());
                roleEntity.setGuildId(guild.getId());
                roleEntity = roleService.save(roleEntity);
            }
            roleEntity.addPermission(permissionEntity);
            roleService.save(roleEntity);
            event.getChannel().sendMessage("Added.").queue();
            return;
        }

        event.getChannel().sendMessage("Bad member or role").queue();
    }

    private void permissionRemove(MessageReceivedEvent event) {
        boolean allowed = isAllowed(event.getMember(), "PERMISSION_MANAGER");
        if (!allowed) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args.length != 3) {
            event.getChannel().sendMessage("Usage: cmd <@member or @role> <permName>").queue();
            return;
        }

        PermissionEntity permissionEntity = permissionService.findByName(args[2]);
        if (permissionEntity == null) {
            event.getChannel().sendMessage("Bad permission").queue();
            return;
        }

        Guild guild = event.getGuild();
        Member member = DiscordId.getMember(guild, args[1]);
        Role role = DiscordId.getRole(guild, args[1]);
        if (member != null) {
            MemberEntity memberEntity = memberService.findByMemberIdAndGuildId(member.getId(), guild.getId());
            if (memberEntity == null) {
                event.getChannel().sendMessage("Bad member").queue();
                return;
            }
            memberEntity.removePermission(permissionEntity);
            memberService.save(memberEntity);
            event.getChannel().sendMessage("Removed.").queue();
            return;
        }

        if (role != null) {
            RoleEntity roleEntity = roleService.findByRoleIdAndGuildId(role.getId(), guild.getId());
            if (roleEntity == null) {
                event.getChannel().sendMessage("Bad role").queue();
                return;
            }
            roleEntity.removePermission(permissionEntity);
            roleService.save(roleEntity);
            event.getChannel().sendMessage("Removed.").queue();
            return;
        }

        event.getChannel().sendMessage("Bad member or role").queue();
    }
}
