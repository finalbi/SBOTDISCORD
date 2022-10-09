package org.finalbi.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.finalbi.Main;
import org.jetbrains.annotations.NotNull;

public class DevCommands extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("shutdown")) {
            if (event.getMember().getRoles().contains(event.getGuild().getRoleById("1026608117779288074"))) {
                if (!event.getOptions().isEmpty()) {
                    event.reply("shutting down").queue();
                    System.exit((int) event.getOption("status").getAsDouble());
                } else {
                    event.reply("shutting down").queue();
                    System.exit(0);
                }
            } else {
                event.reply(event.getUser().getName() + " You do not have the correct permissions to perform this action").setEphemeral(true).queue();
            }
        } else if (event.getName().equals("save")) {
            if (event.getMember().getRoles().contains(event.getGuild().getRoleById("1026608117779288074"))) {
                Main.manger.save();
                event.reply("saved").queue();
            } else {
                event.reply(event.getUser().getName() + " You do not have the correct permissions to perform this action").setEphemeral(true).queue();
            }
        } else if (event.getName().equals("xpmultiplier")) {
            if (event.getMember().getRoles().contains(event.getGuild().getRoleById("1026608117779288074"))) {
                Main.manger.xpGainMultiplier = (int)event.getOption("value").getAsDouble();
                event.reply("set multiplier to " + (int)event.getOption("value").getAsDouble()).queue();
            } else {
                event.reply(event.getUser().getName() + " You do not have the correct permissions to perform this action").setEphemeral(true).queue();
            }
        }
    }
}