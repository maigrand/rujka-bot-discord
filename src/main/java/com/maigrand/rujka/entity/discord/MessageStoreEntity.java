package com.maigrand.rujka.entity.discord;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "discord_message_store")
@NoArgsConstructor
@Data
public class MessageStoreEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "discord_user_id")
    private String userId;

    @Column(name = "discord_guild_id")
    private String guildId;

    @Column(name = "discord_channel_id")
    private String channelId;

    @Column(name = "discord_message_id")
    private String messageId;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at", updatable = false)
    @Setter(AccessLevel.NONE)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}
