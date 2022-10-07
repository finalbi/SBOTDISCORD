package org.finalbi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.internal.entities.UserById;
import org.finalbi.commands.*;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;

import java.io.IOException;
import java.util.*;
public class Main {

    public static JDA jda;
    public static void main(String[] args) throws LoginException, IOException {

        XPManger manger = new XPManger();
        manger.add(new UserById(75845363918189365L), 15);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> manger.save()));

//        System.out.println("Starting...");
//        jda = JDABuilder.createLight("MTAyMTYyMTgxODczOTY1NDY5Ng.Gl-w_g.OkYVzB4LM3oyXhRts-u4G5w9STZeUR-aui2lzA",  GUILD_MESSAGES, GUILD_VOICE_STATES).setActivity(Activity.playing("Selkirk Student Simulator")).addEventListeners(new EvalCommands(), new GameCommands(), new DMMANGER(), new DevCommands()).build();
//        System.out.println("Online");
//        jda.upsertCommand("ping", "Calculates the ping of the bot").queue();
//        jda.upsertCommand("rps", "Rock Paper Scissors").queue();
//        jda.upsertCommand("shutdown", "shuts the bot down").addOption(OptionType.NUMBER, "status", "the exit status of the bot", false).queue();
//        System.out.println("Registered Commands");
    }





}
