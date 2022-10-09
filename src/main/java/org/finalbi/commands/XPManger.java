package org.finalbi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.finalbi.Main;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class XPManger extends ListenerAdapter {

    File xpFile;

    Map<User, Level> levels;

    public int xpGainMultiplier;



    public XPManger() {
        levels = new HashMap<>();
        xpFile = new File("xp.txt");
    }

    public void add(User user, int amount){
        levels.putIfAbsent(user, new Level());
        Level level = levels.get(user);
        level.addXP(amount);
        levels.put(user, level);
    }


    public void addNew(User user, Level level){
        levels.put(user, level);
    }

    public void addNew(User user){
        levels.put(user, new Level());
    }

    public void save() {

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(xpFile));

            // create new BufferedWriter for the output file
            // iterate map entries
            for (Map.Entry<User, Level
                    > entry :
                    levels.entrySet()) {
                // put key and value separated by a colon
                bw.write(entry.getKey().getId() + ":"
                        + entry.getValue().amountRequiredForLevel + ":" + entry.getValue().level + ":" + entry.getValue().xp);

                // new line
                bw.newLine();
            }

            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                // always close the writer
                bw.close();
            }
            catch (Exception e) {
            }
        }
    }

    public void load(){

            Map<User, Level> map
                    = new HashMap<User, Level>();
        BufferedReader br = null;

            try {
                br = new BufferedReader(new FileReader(xpFile));
                String line = null;
                // read file line by line
                while ((line = br.readLine()) != null) {

                    // split the line by :
                    String[] parts = line.split(":");

                    // first part is name, second is number
                    User user = User.fromId(parts[0].trim());
                    int amountrequiredforlevel = Integer.parseInt(parts[1].trim());
                    int level = Integer.parseInt(parts[2].trim());
                    int xp = Integer.parseInt(parts[3].trim());

                    // put name, number in HashMap if they are
                    // not empty
                        map.put(user, new Level(xp, level, amountrequiredforlevel));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {

                // Always close the BufferedReader
                if (br != null) {
                    try {
                        br.close();
                    }
                    catch (Exception e) {
                    };
                }
            }
            levels = map;
    }

    public void remove(User user, int amount){
        if (levels.containsValue(user)){
            Level level = levels.get(user);
            level.removeXP(amount);
            levels.put(user, level);
        }

    }

    public Map<User, Level> getLevels() {
        return levels;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        add(event.getAuthor(), xpGainMultiplier);
        if (levels.get(event.getAuthor()).levelUpNeedsToBeProcessed){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.YELLOW);
            builder.addField("name", event.getAuthor().getName(), false);
            builder.addField("levelMessage", "has leveled up to:", false);
            builder.addField("level", String.valueOf(levels.get(event.getAuthor()).level), false);
            builder.setThumbnail(event.getAuthor().getAvatarUrl());
            builder.setAuthor("Selkirk Bot", null, null);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("getlevel")){
            if (levels.containsValue(event.getUser())) {
                addNew(event.getUser());
            }
            JProgressBar progressBar = new JProgressBar(0, levels.get(event.getUser()).amountRequiredForLevel);
            progressBar.setValue(levels.get(event.getUser()).xp);
            Image image = progressBar.createImage(100, 100);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.YELLOW);
            builder.addField("xp", "xp: " + levels.get(event.getUser()).xp, false);
            builder.addField("level", "levels: " + levels.get(event.getUser()).level, false);
            builder.
            event.replyEmbeds(builder.build()).queue();
        }
    }
}
