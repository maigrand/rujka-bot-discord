package com.maigrand.rujka.entity.discord;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "discord_role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"role_id", "guild_id"})
        })
@NoArgsConstructor
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "guild_id")
    private String guildId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "discord_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private final Set<PermissionEntity> permissionEntitySet = new HashSet<>();

    public void addPermission(PermissionEntity entity) {
        this.permissionEntitySet.add(entity);
    }
}
