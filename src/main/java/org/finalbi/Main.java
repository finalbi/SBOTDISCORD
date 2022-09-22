package org.finalbi;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
public class Main extends ListenerAdapter {

    static AudioPlayerManager manger;
    public static void main(String[] args) {

        System.out.println("Starting...");
        manger = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(manger);
        JDA jda = JDABuilder.createLight(System.getenv("TOKEN"), Collections.emptyList()).setActivity(Activity.playing("Selkirk Student Simulator")).addEventListeners(new Main()).build();
        System.out.println("Online");
        jda.upsertCommand("ping", "Calculates the ping of the bot").queue();
        jda.upsertCommand("rps", "Rock Paper Scissors").queue();
        jda.upsertCommand("play", "plays a song").addOption(OptionType.STRING, "Link", "a link to the youtube video").queue();
        System.out.println("Registered Commands");
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

    public void playSong(SlashCommandInteractionEvent event){
        String url = event.getOption("link").getAsString();
        AudioPlayer player = manger.createPlayer();
        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);
    }
}