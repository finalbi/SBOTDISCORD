package org.finalbi.commands;

import com.google.gson.Gson;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.WidgetUtil;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.finalbi.memegen.Meme;
import org.finalbi.music.AudioPlayerSendHandler;
import org.finalbi.music.TrackScheduler;
import org.finalbi.util.Base64Encoder;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class MiscCommands extends ListenerAdapter {

    AudioPlayerManager playerManager;
    public MiscCommands() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

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
        }else if (event.getName().equals("play")) {
            AudioPlayer player = playerManager.createPlayer();
            TrackScheduler scheduler = new TrackScheduler(player);
            player.addListener(scheduler);

            Guild guild = event.getGuild();
            VoiceChannel channel = guild.getVoiceChannelById(1037417912857796668L);
            AudioManager manger = guild.getAudioManager();

            manger.setSendingHandler(new AudioPlayerSendHandler(player));
            manger.openAudioConnection(channel);
            playerManager.loadItem(event.getOption("url").getAsString(), new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    scheduler.queue(track);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        scheduler.queue(track);
                    }
                }

                @Override
                public void noMatches() {
                    // Notify the user that we've got nothing
                }

                @Override
                public void loadFailed(FriendlyException throwable) {
                    // Notify the user that everything exploded
                }
            });
        }else if (event.getName().equals("meme")){
            Meme meme;
            Gson gson = new Gson();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://meme-api.herokuapp.com/gimme/wholesomememes"))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = null;
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {

            }
            meme = gson.fromJson(response.body(), Meme.class);
            while (meme.getLink() == null){
                HttpRequest newrequest = HttpRequest.newBuilder()
                        .uri(URI.create("https://meme-api.herokuapp.com/gimme/wholesomememes"))
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                try {
                    response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                } catch (Exception e) {

                }
                meme = gson.fromJson(response.body(), Meme.class);
            }
            try {
                URL url = new URL(meme.getLink());
                BufferedImage img = ImageIO.read(url);
                File file = new File("temp.jpg"); // change the '.jpg' to whatever extension the image has
                ImageIO.write(img, "jpg", file); // again, change 'jpg' to the correct extension
                event.getChannel().sendFiles(FileUpload.fromData(file)).queue();
            }catch (Exception e){

            }
        }

    }
}
