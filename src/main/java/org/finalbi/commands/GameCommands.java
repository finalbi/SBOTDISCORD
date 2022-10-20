package org.finalbi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameCommands extends ListenerAdapter {

    private EmbedBuilder builder;
    public List<User> QuedGames;

    public GameCommands(){
        QuedGames = new ArrayList<>();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("rps")) {
            builder = new EmbedBuilder();
            builder.setColor(Color.YELLOW);
            builder.addField(" ", "Rock Paper or Scissors", false);
            builder.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Rock-paper-scissors.svg/1200px-Rock-paper-scissors.svg.png");
            event.replyEmbeds(builder.build()).addActionRow(
                    Button.secondary("rock", "Rock"),
                    Button.primary("paper", "Paper"),
                    Button.success("scissors", "Scissors")
            ).queue();
        }else if (event.getName().equals("multiplayerrps")){

        }else if (event.getName().equals("accept")){

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
        builder.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Rock-paper-scissors.svg/1200px-Rock-paper-scissors.svg.png");
        if (value.equals(playerAnswer)) {
            builder.addField(" ","you picked " + playerAnswer + " i picked " + value + " it is a tie", false);
        } else if (value.equals("rock") && playerAnswer.equals("paper")) {
            builder.addField(" ","you picked " + playerAnswer + " i picked " + value + " You Won", false);
        } else if (value.equals("paper") && playerAnswer.equals("scissors")) {
            builder.addField(" ","you picked " + playerAnswer + " i picked " + value + " You Won", false);
        } else if (value.equals("scissors") && playerAnswer.equals("rock")) {
            builder.addField(" ","you picked " + playerAnswer + " i picked " + value + " You Won", false);
        } else if (value.equals("rock") && playerAnswer.equals("scissors")) {
            builder.addField(" ","you picked " + playerAnswer + " i picked " + value + " You Lost", false);
        } else if (value.equals("scissors") && playerAnswer.equals("paper")) {
            builder.addField(" ","you picked " + playerAnswer + " i picked " + value + " You Lost", false);
        } else if (value.equals("paper") && playerAnswer.equals("rock")) {
            builder.addField(" ", "you picked " + playerAnswer + " i picked " + value + " You lost", false);
        }
        event.editMessageEmbeds(builder.build()).queue();
    }

    public void multirps(SlashCommandEvent event){

    }
}
