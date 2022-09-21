package org.finalbi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main extends ListenerAdapter {
    int rpslastoutput;
    public static void main(String[] args) {
        System.out.println("Starting...");

        JDA jda = JDABuilder.createLight(System.getenv("TOKEN"), Collections.emptyList()).setActivity(Activity.playing("Selkirk Student Simulator")).addEventListeners(new Main()).build();
        System.out.println("Online");
        jda.upsertCommand("ping", "Calculates the ping of the bot").queue();
        jda.upsertCommand("rps", "Rock Paper Scissors").queue();
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
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("rock")){
            RPS("rock", event);
        } else if (event.getComponentId().equals("paper")) {
            RPS("paper", event);
        }else if (event.getComponentId().equals("scissors")) {
            RPS("scissors", event);
        }
    }

    public void Pong(SlashCommandInteractionEvent event){
        long time = System.currentTimeMillis();
        event.reply("Pong").setEphemeral(false).flatMap(v -> event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)).queue();
    }

    public void RPS(String playerInput, ButtonInteractionEvent event) {
        Random rand = new Random();
        String playerAnswer = playerInput;
        List<String> Answers = new ArrayList<String>();
        Answers.add(0, "rock");
        Answers.add(1, "paper");
        Answers.add(2, "scissors");
        int answerValue = (int)(Math.random()*3);
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
        } else if (value.equals("scissors") && playerAnswer.equals("paper")){
            event.reply("you picked " + playerAnswer + " i picked " + value + " You Lost").queue();
        } else if (value.equals("paper") && playerAnswer.equals("rock")) {
            event.reply("you picked " + playerAnswer + " i picked " + value + " You lost").queue();
        }
    }

}