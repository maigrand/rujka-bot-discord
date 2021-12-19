package com.maigrand.rujka.discord.util;

import net.dv8tion.jda.api.entities.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordId {

    public static Member getMember(Guild guild, String memberString) {
        Pattern pattern = Pattern.compile("^\\s*?<@!?(\\d+)>\\s*?$|^\\s*?(\\d+)\\s*?$");
        Matcher matcher = pattern.matcher(memberString);
        if (matcher.find()) {
            return matcher.group(1) == null ? guild.getMemberById(matcher.group(2)) : guild.getMemberById(matcher.group(1));
        }
        return null;
    }

    public static Role getRole(Guild guild, String roleString) {
        Pattern pattern = Pattern.compile("^\\s*?<@&(\\d+)>\\s*?$|\\s*?(\\d+)\\s*?$");
        Matcher matcher = pattern.matcher(roleString);
        if (matcher.find()) {
            return matcher.group(1) == null ? guild.getRoleById(matcher.group(2)) : guild.getRoleById(matcher.group(1));
        }
        return null;
    }

    public static TextChannel getTextChannel(Guild guild, String textChannelString) {
        Pattern pattern = Pattern.compile("^\\s*?<#(\\d+)>\\s*?$|\\s*?(\\d+)\\s*?$");
        Matcher matcher = pattern.matcher(textChannelString);
        if (matcher.find()) {
            return matcher.group(1) == null ? guild.getTextChannelById(matcher.group(2)) : guild.getTextChannelById(matcher.group(1));
        }
        return null;
    }
}
