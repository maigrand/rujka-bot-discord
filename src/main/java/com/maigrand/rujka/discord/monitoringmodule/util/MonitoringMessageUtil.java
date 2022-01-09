package com.maigrand.rujka.discord.monitoringmodule.util;

import com.maigrand.rujka.discord.util.GetStatusNet;
import com.maigrand.rujka.entity.discord.MonitoringEntity;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MonitoringMessageUtil {

    private final MonitoringEntity entity;

    public void update(Message message) {
        EmbedBuilder emb = new EmbedBuilder();
        String host = entity.getServerAddress().split(":")[0];
        int port = Integer.parseInt(entity.getServerAddress().split(":")[1]);
        String statusNet = GetStatusNet.getStatusNet(host, port);

        if (statusNet.equals("offline")) {
            emb.setTitle("\uD83D\uDEAB " + entity.getServerName());
            message.editMessageEmbeds(emb.build()).queue();
            return;
        }

        if (statusNet.split("\n").length <= 3) {
            String gameTypeField = getGameTypeField(statusNet);
            String footer = getFooter(entity.getServerAddress(), entity.getServerPassword());

            String emoteOnline = "\uD83D\uDFE2";
            try {
                Emote emoteById = message.getGuild().getEmotesByName("greendot", true).get(0);
                emoteOnline = emoteById.getAsMention();
            } catch (IndexOutOfBoundsException ignored) {
            }

            String maxClients = getVariables("sv_maxclients", statusNet);
            String hostName = getVariables("sv_hostname", statusNet).replaceAll("\\^\\d", "");

            emb.setTitle(emoteOnline + "  **0/" + maxClients + "**  |  **" + gameTypeField + "**  |  **" + hostName + "**");
            emb.setFooter(footer);
            emb.setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()));

            message.editMessageEmbeds(emb.build())
                    .retainFilesById(new ArrayList<>())
                    .queue();
            return;
        }

        String players = getPlayersField(statusNet).get("value");
        if (players.length() > 2000) {
            players = players.substring(0, 2000);
        }

        emb.setAuthor(getAuthor(statusNet));
        emb.setTitle(entity.getServerAddress());
        emb.addField("Map", getMapField(statusNet), true);
        emb.addField("Gametype", getGameTypeField(statusNet), true);
        emb.addField("Fraglimit", getVariables("fraglimit", statusNet), true);
        emb.addField("Timelimit", getVariables("timelimit", statusNet), true);
        emb.addField(getPlayersField(statusNet).get("name"), players, false);
        emb.setFooter(getFooter(entity.getServerAddress(), entity.getServerPassword()), null);
        emb.setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()));

        if (statusNet.split("\n").length == 3) {
            emb.setColor(new Color(10, 70, 10));
        } else {
            emb.setColor(new Color(10, 160, 10));
        }

        String mapField = getMapField(statusNet);
        String mapFileName = mapField.replaceAll("/", "") + ".jpg";

        File mapThumbnail = getMapThumbnail(mapFileName);
        if (mapThumbnail != null) {
            emb.setThumbnail("attachment://map.jpg");
            message.editMessageEmbeds(emb.build())
                    .retainFilesById(new ArrayList<>())
                    .addFile(mapThumbnail, "map.jpg")
                    .queue();
            return;
        }

        message.editMessageEmbeds(emb.build())
                .retainFilesById(new ArrayList<>())
                .queue();
    }

    private String getVariables(String var, String statusNet) {
        Matcher matcher;
        try {
            matcher = Pattern.compile("\\\\" + var + "\\\\(.+?)\\\\").matcher(statusNet.split("\n")[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private String getAuthor(String statusNet) {
        String emotePassword = "";
        if (getVariables("g_needpass", statusNet).equals("1")) {
            emotePassword = "\uD83D\uDD12";
        }
        return getVariables("sv_hostname", statusNet).replaceAll("\\^\\d", "") + emotePassword;
    }

    private String getMapField(String statusNet) {
        return getVariables("mapname", statusNet).replaceAll("\\^\\d", "").toLowerCase();
    }

    private String getGameTypeField(String statusNet) {
        String gameType = getVariables("g_gametype", statusNet);
        switch (gameType) {
            case "0":
                gameType = "FFA";
                break;
            case "3":
                gameType = "DUEL";
                break;
            case "6":
                gameType = "TFFA";
                break;
            case "4":
                gameType = "POWER DUEL";
                break;
            case "8":
                gameType = "CTF";
                break;
            case "7":
                gameType = "SIEGE";
                break;
        }
        return gameType;
    }

    private Map<String, String> getPlayersField(String statusNet) {
        Map<String, String> playersMap = new HashMap<>();
        String[] status = statusNet.split("\n");
        StringBuilder playersStringBuilder = new StringBuilder();
        int indexPlayer = 1;
        for (int i = 2; i < status.length - 1; i++) {
            String[] split = status[i].split("\\d\\s\"");
            try {
                if (split[1].contains("*")) {
                    split[1] = split[1].replace("*", "\\*");
                }
                if (split[1].contains("_")) {
                    split[1] = split[1].replace("_", "\\_");
                }
                if (split[1].contains("\"")) {
                    split[1] = split[1].replace("\"", "");
                }
                if (split[1].contains("|")) {
                    split[1] = split[1].replace("|", "\\|");
                }
                if (split[1].contains("discord.gg")) {
                    split[1] = split[1].replaceAll(".+", "discord.gg");
                }
                playersStringBuilder.append(indexPlayer).append(") ").append(split[1]).append(" (score: ")
                        .append(status[i].split("\\s+")[0]).append(")").append("\n");
                indexPlayer++;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        if (status.length == 3) {
            playersStringBuilder.append("-");
        }
        String players = String.valueOf(playersStringBuilder).replaceAll("\\^\\d", "");
        playersMap.put("name", ":white_check_mark: Online " + (status.length - 3) + "/" + getVariables("sv_maxclients", statusNet));
        playersMap.put("value", players);
        return playersMap;
    }

    private String getFooter(String address, String password) {
        switch (password) {
            case "null":
                return "/connect " + address;
            case "jof":
                return "На сервере используется japlus мод, а так же нестандартные карты, скачать их можно здесь - https://www.dropbox.com/sh/dmohr8oq946tflc/AADbwg6l3TBHdRAFGpEMvFw2a";
            case "whoracle":
                return "Чтобы зайти, скачайте отсюда архив и распакуйте в папку base: http://bit.ly/2MQ6oba";
            default:
                return "/connect " + address + ";password " + password;
        }
    }

    private File getMapThumbnail(String mapFileName) {
        try {
            return ResourceUtils.getFile("classpath:discordmonitoringimages/" + mapFileName);
        } catch (FileNotFoundException e) {
            try {
                return ResourceUtils.getFile("classpath:discordmonitoringimages/cat.png");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
