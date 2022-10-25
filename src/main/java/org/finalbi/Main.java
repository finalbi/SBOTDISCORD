package org.finalbi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import org.finalbi.commands.*;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;

import java.io.IOException;
import java.util.*;
public class Main {

    public static JDA jda;
    public static XPManger manger;
    public static void main(String[] args) throws LoginException, IOException {
        manger = new XPManger();

        Runtime.getRuntime().addShutdownHook(new Thread(manger::save));

        System.out.println("Starting...");
        jda = JDABuilder.createLight("MTAyMTYyMTgxODczOTY1NDY5Ng.Gl-w_g.OkYVzB4LM3oyXhRts-u4G5w9STZeUR-aui2lzA",  GUILD_MESSAGES, GUILD_VOICE_STATES).setActivity(Activity.playing("Selkirk Student Simulator")).addEventListeners(new EvalCommands(), new GameCommands(), new DMMANGER(), new DevCommands(), new MiscCommands(), manger).build();
        System.out.println("Online");
        jda.upsertCommand("ping", "Calculates the ping of the bot").queue();
        jda.upsertCommand("rps", "Rock Paper Scissors").queue();
        jda.upsertCommand("shutdown", "shuts the bot down").addOption(OptionType.NUMBER, "status", "the exit status of the bot", false).queue();
        jda.upsertCommand("save", "saves the xp data").queue();
        jda.upsertCommand("xpmultiplier", "sets the xp Multiplier").addOption(OptionType.NUMBER, "value", "the value to set it to").queue();
        jda.upsertCommand("getlevel", "gets your xp and level").addOption(OptionType.USER, "user", "the user that you are viewing is optional", false).queue();
        jda.upsertCommand("base64", "encodes or decode text from base64").addOption(OptionType.BOOLEAN, "encode", "if this is true it encode if it is false it decodes").addOption(OptionType.BOOLEAN, "url", "is this a url or not", false).addOption(OptionType.STRING, "text", "the text to encode or decode").queue();
        jda.upsertCommand("leaderboard", "views the xp leaderboard").queue();
        jda.upsertCommand("accept", "accepts a rock paper scissors request").addOption(OptionType.USER, "user", "the user that sent you the request").addOption(new OptionData(OptionType.STRING, "", "")).queue();
        jda.upsertCommand("multiplayerrps", "makes a game of multiplayer Rock Paper Scissors").addOption(OptionType.USER, "user", "the user you want to play with").addOption(new OptionData(OptionType.STRING, "", "")).queue();
        System.out.println("Registered Commands");
        manger.load();
        System.out.println("Loaded XP data");
    }





}
