package org.finalbi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audio.hooks.ListenerProxy;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.finalbi.Main;

public class FeedBack extends ListenerAdapter {
    private final long channelId, authorId;

    public FeedBack(MessageChannel channel, User author) {
        this.channelId = channel.getIdLong();
        this.authorId = author.getIdLong();
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getAuthor().getIdLong() != authorId) return;
        if (event.getChannel().getIdLong() != channelId) return;
        MessageChannel channel = event.getChannel();
        String content = event.getMessage().getContentRaw();
        event.getChannel().sendMessage("Thanks for your feedback!").queue();
        EmbedBuilder feedback = new EmbedBuilder();
        feedback.setTitle("Automated System Operations - Leaving Feedback");
        feedback.addField("Feedback", content, false);
        feedback.setColor(0xC90004);
        feedback.setAuthor(event.getAuthor().getAsTag()+" - "+event.getAuthor().getId(), "https://support.discord.com/hc/en-us/articles/209572128-How-do-I-log-out-", event.getAuthor().getAvatarUrl());
        feedback.setImage("https://media.discordapp.net/attachments/894913784823566428/896323821336948736/unknown.png?width=384&height=192");
        Main.jda.getGuildById("1026589152491352079").getTextChannelById("1026589153221148744").sendMessageEmbeds(feedback.build()).queue();
        event.getJDA().removeEventListener(this);
    }
}
