package com.maigrand.rujka.entity.discord;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "discord_monitoring")
@NoArgsConstructor
@Data
public class MonitoringEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "guild_id")
    private String guildId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "server_address")
    private String serverAddress;

    @Column(name = "server_name")
    private String serverName;

    @Column(name = "server_password")
    private String serverPassword;

    @Column(name = "server_index")
    private Integer serverIndex;
}
