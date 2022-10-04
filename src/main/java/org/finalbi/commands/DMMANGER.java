package org.finalbi.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DMMANGER extends ListenerAdapter {

    private final String DevModePassword = "amongus";
    Guild guild;

    TextChannel channel;

    JDA jda;

    public DMMANGER(TextChannel channel, JDA jda) {
        this.jda = jda;
        this.channel = channel;
        guild = jda.getGuildById("1026589152491352079");
    }

    public DMMANGER() {

    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("devmode")) {
            String guildName = event.getGuild().getName();
            event.getUser().openPrivateChannel().flatMap(channel -> {
                event.getJDA().addEventListener(this);
                return channel.sendMessage("Hello, we are sorry you're leaving" + guildName + ", if you don't mind, please tell us why you left or leave any other feedback here, it'll help us improve the server and improve experience for you if you re-join again in the future.\\n\\nThank you.");
            }).queue();
        }
    }




}