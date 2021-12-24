package com.maigrand.rujka.repository.discord;

import com.maigrand.rujka.entity.discord.MessageStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageStoreRepository extends JpaRepository<MessageStoreEntity, Integer> {

    Optional<MessageStoreEntity> findByGuildIdAndChannelIdAndMessageId(String guildId,
            String channelId,
            String messageId);
}
