package org.finalbi;

import com.fasterxml.jackson.databind.JsonNode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.runtime.ObjectMethods;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {

        System.out.println(generateQuote());

//        System.out.println("Starting...");
//
//        JDA jda = JDABuilder.createLight(System.getenv("TOKEN"), Collections.emptyList()).setActivity(Activity.playing("Selkirk Student Simulator")).addEventListeners(new Main()).build();
//        System.out.println("Online");
//        jda.upsertCommand("ping", "Calculates the ping of the bot").queue();
//        jda.upsertCommand("rps", "Rock Paper Scissors").queue();
//        jda.upsertCommand("quote", "generates a famous quote").queue();
//        System.out.println("Registered Commands");
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
        } else if (event.getName().equals("quote")) {
//            event.reply(generateQuote());
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

    public static String generateQuote() {
        try {
            URL url = new URL("https://type.fit/api/quotes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("count", "1");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            return content.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "FAILED";
    }


}