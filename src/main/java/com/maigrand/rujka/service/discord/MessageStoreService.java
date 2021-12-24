package com.maigrand.rujka.service.discord;

import com.maigrand.rujka.entity.discord.MessageStoreEntity;
import com.maigrand.rujka.repository.discord.MessageStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class MessageStoreService {

    private final MessageStoreRepository messageStoreRepository;

    public List<MessageStoreEntity> findAll() {
        return messageStoreRepository.findAll();
    }

    public MessageStoreEntity findById(int id) {
        return messageStoreRepository.findById(id)
                .orElse(null);
    }

    public MessageStoreEntity findByGuildIdAndChannelIdAndMessageId(String guildId,
            String channelId,
            String messageId) {
        return messageStoreRepository.findByGuildIdAndChannelIdAndMessageId(guildId, channelId, messageId)
                .orElse(null);
    }

    public MessageStoreEntity save(MessageStoreEntity entity) {
        return messageStoreRepository.save(entity);
    }

    public void delete(MessageStoreEntity entity) {
        messageStoreRepository.delete(entity);
    }
}
