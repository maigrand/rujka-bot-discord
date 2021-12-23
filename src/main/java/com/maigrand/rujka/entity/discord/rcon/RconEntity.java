package com.maigrand.rujka.entity.discord.rcon;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "discord_rcon")
@NoArgsConstructor
@Data
public class RconEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Column(name = "server_address", unique = true)
    private String serverAddress;

    @Column(name = "server_name", unique = true)
    private String serverName;

    @Column(name = "rcon_password")
    private String rconPassword;
}
