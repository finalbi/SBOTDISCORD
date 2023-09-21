package org.finalbi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameCommands extends ListenerAdapter {

    private EmbedBuilder builder;
    public Map<User, MRPSGame> QuedGames;

    public GameCommands(){
        QuedGames = new HashMap<>();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
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
            multirps(event);
        }else if (event.getName().equals("accept")){
            accept(event);
        } else if (event.getName().equals("decline")) {
            decline(event);
        }
    }



    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("rock")) {
            RPS("rock", event);
        } else if (event.getComponentId().equals("paper")) {
            RPS("paper", event);
        } else if (event.getComponentId().equals("scissors")) {
            RPS("scissors", event);
        }
    }


    public void RPS(String playerInput, ButtonInteractionEvent event) {
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

    public void multirps(SlashCommandInteractionEvent event){
        QuedGames.put(event.getUser(), new MRPSGame(event.getUser(), event.getOption("user").getAsUser(), event.getOption("awnser").getAsString()));
        MessageCreateBuilder builder1 = new MessageCreateBuilder();
        builder1.mentionUsers(event.getOption("user").getAsUser().getId());
        builder1.mentionUsers(event.getUser().getId());
        builder1.addContent(event.getOption("user").getAsUser().getName() + " you have a game request from " + event.getUser().getName());
        builder1.addContent(" accept or decline the request");
        event.reply(builder1.build()).queue();
    }

    public void accept(SlashCommandInteractionEvent event){
        MRPSGame game =  QuedGames.get(event.getOption("user").getAsUser());
        if (game != null){
            game.accepted = true;
            game.p2Awnser = event.getOption("awnser").getAsString();
            MRPS(event, game);
        } else {
            event.reply("Game does not exist").queue();
        }

    }

    public void decline(SlashCommandInteractionEvent event){
        MRPSGame game =  QuedGames.get(event.getOption("user").getAsUser());
        if (game != null){
            game.accepted = false;
            QuedGames.remove(event.getOption("user").getAsUser());
        }else {
            event.reply("Game does not exist").queue();
        }
    }

    public void MRPS(SlashCommandInteractionEvent event, MRPSGame game){
        EmbedBuilder Mbuilder = new EmbedBuilder();
        String p1Awn = game.p1Awnser;
        String p2Awn = game.p2Awnser;
        String p1Name = game.p1.getName();
        String p2Name = game.p2.getName();
        Mbuilder.setThumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/6/67/Rock-paper-scissors.svg/1200px-Rock-paper-scissors.svg.png");
        if (p1Awn.equals(p2Awn)) {
            Mbuilder.addField(" ", p2Name + " picked " + p2Awn + " " + p1Name + " picked " + p1Awn + " it is a tie", false);
        } else if (p1Awn.equals("rock") && p2Awn.equals("paper")) {
            Mbuilder.addField(" ", p2Name + " picked " + p2Awn +  " " + p1Name + " picked " + p1Awn + " " + p2Name + " Won", false);
        } else if (p1Awn.equals("paper") && p2Awn.equals("scissors")) {
            Mbuilder.addField(" ", p2Name + " picked " + p2Awn + " " + p1Name + " picked " + p1Awn + " " + p1Name + " Won", false);
        } else if (p1Awn.equals("scissors") && p2Awn.equals("rock")) {
            Mbuilder.addField(" ", p2Name + " picked " + p2Awn + " " + p1Name + " picked " + p1Awn + " " + p1Name + " Won", false);
        } else if (p1Awn.equals("rock") && p2Awn.equals("scissors")) {
            Mbuilder.addField(" ", p2Name + " picked " + p2Awn + " " + p1Name + " picked " + p1Awn + " " + p1Name + " Won", false);
        } else if (p1Awn.equals("scissors") && p2Awn.equals("paper")) {
            Mbuilder.addField(" ", p2Name + " picked " + p2Awn + " " + p1Name + " picked " + p1Awn + " " + p1Name + " Won", false);
        } else if (p1Awn.equals("paper") && p2Awn.equals("rock")) {
            Mbuilder.addField(" ", p2Name + " picked " + p2Awn + " " + p1Name + " picked " + p1Awn + " " + p1Name + " Won", false);
        }
        event.replyEmbeds(Mbuilder.build()).queue();
    }

    public void XO(SlashCommandInteractionEvent event){

    }
}
