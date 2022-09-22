package org.finalbi;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;

import java.util.*;
public class Main extends ListenerAdapter {

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    public static void main(String[] args) {

        System.out.println("Starting...");
        JDA jda = JDABuilder.createLight(System.getenv("TOKEN"),  GUILD_MESSAGES, GUILD_VOICE_STATES).setActivity(Activity.playing("Selkirk Student Simulator")).addEventListeners(new Main()).build();
        System.out.println("Online");
        jda.upsertCommand("ping", "Calculates the ping of the bot").queue();
        jda.upsertCommand("rps", "Rock Paper Scissors").queue();
        jda.upsertCommand("play", "plays a song").addOption(OptionType.STRING, "Link", "a link to the youtube video").queue();
        jda.upsertCommand("skip", "skips this song").queue();
        System.out.println("Registered Commands");
    }

    public Main() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            Pong(event);
        } else if (event.getName().equals("rps")) {
            event.reply("Pick Your Choice").addActionRow(
                    Button.secondary("rock", "Rock"),
                    Button.primary("paper", "Paper"),
                    Button.success("scissors", "Scissors")
            ).queue();
        } else if (event.getName().equals("play")) {
            playSong(event);
        }else if (event.getName().equals("skip")){

        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("rock")) {
            RPS("rock", event);
        } else if (event.getComponentId().equals("paper")) {
            RPS("paper", event);
        } else if (event.getComponentId().equals("scissors")) {
            RPS("scissors", event);
        }
    }

    public void Pong(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong").setEphemeral(false).flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)).queue();
    }

    public void RPS(String playerInput, ButtonInteractionEvent event) {
        String playerAnswer = playerInput;
        List<String> Answers = new ArrayList<>();
        Answers.add(0, "rock");
        Answers.add(1, "paper");
        Answers.add(2, "scissors");
        int answerValue = (int) (Math.random() * 3);
        String value = Answers.get(answerValue);
        if (value.equals(playerAnswer)) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " it is a tie").queue();
        } else if (value.equals("rock") && playerAnswer.equals("paper")) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " You Won").queue();
        } else if (value.equals("paper") && playerAnswer.equals("scissors")) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " You Won").queue();
        } else if (value.equals("scissors") && playerAnswer.equals("rock")) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " You Won").queue();
        } else if (value.equals("rock") && playerAnswer.equals("scissors")) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " You Lost").queue();
        } else if (value.equals("scissors") && playerAnswer.equals("paper")) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " You Lost").queue();
        } else if (value.equals("paper") && playerAnswer.equals("rock")) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " You lost").queue();
        }
    }

    private void loadAndPlay(final TextChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

                play(channel.getGuild(), musicManager, track);
            }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    AudioTrack firstTrack = playlist.getSelectedTrack();

                    if (firstTrack == null) {
                        firstTrack = playlist.getTracks().get(0);
                    }

                    channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
                    play(channel.getGuild(), musicManager, firstTrack);
                }

                @Override
                public void noMatches() {
                    channel.sendMessage("Nothing found by " + trackUrl).queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    channel.sendMessage("Could not play: " + exception.getMessage()).queue();
                }
            });
        }

    private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());

        musicManager.scheduler.queue(track);
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    private static void connectToFirstVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isConnected()) {
            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                audioManager.openAudioConnection(voiceChannel);
                break;
            }
        }
    }


}
