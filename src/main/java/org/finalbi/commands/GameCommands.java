package org.finalbi.commands;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GameCommands extends ListenerAdapter {


    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("rps")) {
            event.reply("Pick Your Choice").addActionRow(
                    Button.secondary("rock", "Rock"),
                    Button.primary("paper", "Paper"),
                    Button.success("scissors", "Scissors")
            ).queue();
        }
    }



    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("rock")) {
            RPS("rock", event);
        } else if (event.getComponentId().equals("paper")) {
            RPS("paper", event);
        } else if (event.getComponentId().equals("scissors")) {
            RPS("scissors", event);
        }
    }


    public void RPS(String playerInput, ButtonClickEvent event) {
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
}
