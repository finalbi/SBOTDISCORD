package org.finalbi.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.finalbi.util.Base64Encoder;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class MiscCommands extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("base64")){
                String text = event.getOption("text").getAsString();
                if (event.getOption("url") != null && event.getOption("url").getAsBoolean()) {
                    if (event.getOption("encode").getAsBoolean()) {
                        String encodeResults = Base64.getUrlEncoder().encodeToString(text.getBytes());
                        event.reply("Here is the encoded text: " + encodeResults).queue();
                    } else {
                        byte[] decode = Base64.getUrlDecoder().decode(text.getBytes());
                        String decodeResults = new String(decode);
                        event.reply("here is the decoded text: " + decodeResults).queue();
                    }
                } else {
                    if (event.getOption("encode").getAsBoolean()) {
                        String encodeResults = Base64.getEncoder().encodeToString(text.getBytes());
                        event.reply("Here is the encoded text: " + encodeResults).queue();
                    } else {
                        byte[] decode = Base64.getDecoder().decode(text.getBytes());
                        String decodeResults = new String(decode);
                        event.reply("here is the decoded text: " + decodeResults).queue();
                    }
                }
        }
    }
}
