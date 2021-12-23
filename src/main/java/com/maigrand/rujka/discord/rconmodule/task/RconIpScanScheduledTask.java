package com.maigrand.rujka.discord.rconmodule.task;

import com.maigrand.rujka.discord.rconmodule.util.RconUtil;
import com.maigrand.rujka.entity.discord.rcon.RconEntity;
import com.maigrand.rujka.entity.discord.rcon.RconIpScanIpEntity;
import com.maigrand.rujka.service.discord.rcon.RconService;
import com.maigrand.rujka.service.discord.rcon.RconIpScanIpService;
import com.maigrand.rujka.service.discord.rcon.RconIpScanNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RconIpScanScheduledTask {

    private final int TECHNICAL_STATUS_RESPONSE_COLUMN_COUNT = 9;

    private final int IP_COLUMN_INDEX = 4;

    private final RconService rconService;

    private final RconIpScanIpService rconIpScanIpService;

    private final RconIpScanNameService rconIpScanNameService;

    @Scheduled(cron = "0 */5 * * * *")
    private void execute() throws SocketException, UnknownHostException {
        List<RconEntity> all = rconService.findAll();

        for (RconEntity ent : all) {
            String[] serverAddress = ent.getServerAddress().split(":");
            RconUtil rconUtil = new RconUtil(serverAddress[0], Integer.parseInt(serverAddress[1]), ent.getRconPassword());

            String command = "status";
            rconUtil.commandAsnc(command, response -> {
                String[] out = response.split("\n");

                for (int i = TECHNICAL_STATUS_RESPONSE_COLUMN_COUNT; i < out.length; i++) {
                    try {
                        String[] statusOut = out[i].strip().replaceAll("\\s+", ",").split(",");
                        String ip = Arrays.stream(statusOut)
                                .filter(s -> s.matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}:-?[0-9]{1,5}"))
                                .collect(Collectors.joining())
                                .split(":")[0];

                        if (ip == null || ip.isEmpty() || ip.isBlank()) {
                            continue;
                        }

                        String name = "";
                        for (int j = IP_COLUMN_INDEX + 1; j < statusOut.length; j++) {
                            name += statusOut[j];
                        }
                        name = name.replaceAll("\\^\\d", "");

                        RconIpScanIpEntity ipEntity = rconIpScanIpService.create(ip);
                        rconIpScanNameService.create(name, ipEntity);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        continue;
                    }
                }
            });
        }
    }
}
