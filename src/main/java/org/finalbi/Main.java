package org.finalbi;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.finalbi.commands.*;
import org.finalbi.memegen.Meme;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
public class Main {

    public static JDA jda;
    public static XPManger manger;
    public static void main(String[] args) throws Exception {
        manger = new XPManger();

        Runtime.getRuntime().addShutdownHook(new Thread(manger::save));

        System.out.println("Starting...");
        jda = JDABuilder.createLight("THIS IS WHERE THE TOKEN WOULD GO BUT I REMOVED IT FOR OBIVIOUS RESONS",  GUILD_MESSAGES, GUILD_VOICE_STATES).addEventListeners(new EvalCommands(), new GameCommands(), new DMMANGER(), new DevCommands(), new MiscCommands(), manger).build();
        System.out.println("Online");
        jda.upsertCommand("ping", "Calculates the ping of the bot").queue();
        jda.upsertCommand("rps", "Rock Paper Scissors").queue();
        jda.upsertCommand("shutdown", "shuts the bot down").addOption(OptionType.NUMBER, "status", "the exit status of the bot", false).queue();
        jda.upsertCommand("save", "saves the xp data").queue();
        jda.upsertCommand("xpmultiplier", "sets the xp Multiplier").addOption(OptionType.NUMBER, "value", "the value to set it to").queue();
        jda.upsertCommand("getlevel", "gets your xp and level").addOption(OptionType.USER, "user", "the user that you are viewing is optional", false).queue();
        jda.upsertCommand("base64", "encodes or decode text from base64").addOption(OptionType.BOOLEAN, "encode", "if this is true it encode if it is false it decodes").addOption(OptionType.BOOLEAN, "url", "is this a url or not", false).addOption(OptionType.STRING, "text", "the text to encode or decode").queue();
        jda.upsertCommand("leaderboard", "views the xp leaderboard").queue();
        jda.upsertCommand("accept", "accepts a rock paper scissors request").addOption(OptionType.USER, "user", "the user that sent you the request").addOptions(new OptionData(OptionType.STRING, "awnser", "rock paper or scissors").addChoice("Rock", "rock").addChoice("Paper", "paper").addChoice("Scissors", "scissors")).queue();
        jda.upsertCommand("multiplayerrps", "makes a game of multiplayer Rock Paper Scissors").addOption(OptionType.USER, "user", "the user you want to play with").addOptions(new OptionData(OptionType.STRING, "awnser", "rock paper or scissor").addChoice("Rock", "rock").addChoice("Paper", "paper").addChoice("Scissors", "scissors")).queue();
        jda.upsertCommand("decline", "declines a rock paper scissors request").addOption(OptionType.USER, "user", "the user that requested the game").queue();
        jda.upsertCommand("play", "Plays a song in the music vc").addOption(OptionType.STRING, "url", "the youtube url").queue();
        jda.upsertCommand("meme", "generates a meme").queue();
        System.out.println("Registered Commands");
        manger.load();
        System.out.println("Loaded XP data");
    }





}
