package org.finalbi.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DevCommands extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("shutdown") && event.getMember().getRoles().contains(event.getGuild().getRoleById("1026608117779288074"))){
            System.exit(0);
        }
    }
}
