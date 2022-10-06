package org.finalbi.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DevCommands extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("shutdown")){
            if (event.getMember().getRoles().contains(event.getGuild().getRoleById("1026608117779288074"))){
                if (!event.getOptions().isEmpty()) {
                    System.exit((int)event.getOption("status").getAsDouble());
                }else {
                    System.exit(0);
                }
            }else {
                event.reply(event.getUser().getName() + " You do not have the correct permissions to perform this action").setEphemeral(true).queue();
            }
        }
    }
}