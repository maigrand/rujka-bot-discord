package com.maigrand.rujka.discord;

import com.maigrand.rujka.entity.discord.*;
import com.maigrand.rujka.service.JdaService;
import com.maigrand.rujka.service.discord.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DiscordModule extends ListenerAdapter {

    protected final JdaService jdaService;

    private PermissionService permissionService;

    private MemberService memberService;

    private RoleService roleService;

    public DiscordModule(JdaService jdaService) {
        this.jdaService = jdaService;
        JDA jda = jdaService.getJda();
        jda.addEventListener(this);
    }

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    protected void registerPermission(String[] names) {
        for (String name : names) {
            PermissionEntity ent = permissionService.findByName(name);
            if (ent == null) {
                PermissionEntity permissionEntity = new PermissionEntity();
                permissionEntity.setName(name);
                permissionService.save(permissionEntity);
            }
        }
    }

    @PostConstruct
    private void init() {
        registerPermission(new String[]{"GRANT_ADMIN"});
    }

    protected boolean isAllowed(Member member, String permissionName) {
        if (member == null) {
            System.out.println("Member is null (DiscordModule:69) " + permissionName);
            return false;
        }
        return isAllowed(member.getUser().getId(), member.getGuild().getId(), member.getRoles(), permissionName);
    }

    private boolean isAllowed(String userId, String guildId, List<Role> roleList, String permissionName) {
        boolean allow = false;
        PermissionEntity permissionEntity = permissionService.findByName(permissionName);
        PermissionEntity grantAdminPermissionEntity = permissionService.findByName("GRANT_ADMIN");
        if (permissionEntity == null) {
            System.out.println("permission entity null (DiscordModule:66)");
            return false;
        }

        MemberEntity memberEntity = memberService.findByMemberIdAndGuildId(userId, guildId);

        if (memberEntity != null) {
            if (memberEntity.getPermissionEntitySet().contains(permissionEntity)) {
                allow = true;
            }
            if (memberEntity.getPermissionEntitySet().contains(grantAdminPermissionEntity)) {
                allow = true;
            }
        }

        Set<Boolean> roleAllow = roleList.stream()
                .map(Role::getId)
                .map(s -> {
                            RoleEntity roleEntity = roleService.findByRoleIdAndGuildId(s, guildId);
                            if (roleEntity != null) {
                                if (roleEntity.getPermissionEntitySet().contains(permissionEntity)) {
                                    return true;
                                }
                                return roleEntity.getPermissionEntitySet().contains(grantAdminPermissionEntity);
                            }
                            return false;
                        }
                ).collect(Collectors.toSet());

        if (roleAllow.contains(true)) {
            allow = true;
        }

        return allow;
    }
}
