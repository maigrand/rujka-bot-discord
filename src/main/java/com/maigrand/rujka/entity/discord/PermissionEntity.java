package com.maigrand.rujka.entity.discord;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "discord_permission")
@NoArgsConstructor
@Data
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;
}
