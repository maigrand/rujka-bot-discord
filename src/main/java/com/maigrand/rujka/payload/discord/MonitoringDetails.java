package com.maigrand.rujka.payload.discord;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MonitoringDetails {

    private String guildId;

    private String channelId;

    private String messageId;

    private String serverAddress;

    private String serverName;

    private String serverPassword;

    private Integer serverIndex;
}
