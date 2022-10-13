package org.finalbi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.finalbi.Main;
import org.finalbi.util.Base64Encoder;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

public class XPManger extends ListenerAdapter {

    File xpFile;

    Map<User, Level> levels;

    public int xpGainMultiplier = 3;



    public XPManger() {
        levels = new HashMap<>();
        xpFile = new File("xp.txt");
    }

    public void add(User user, int amount){
        levels.putIfAbsent(user, new Level(user));
        Level level = levels.get(user);
        level.addXP(amount);
        levels.put(user, level);
    }


    public void addNew(User user, Level level){
        levels.put(user, level);
    }

    public void addNew(User user){
        levels.put(user, new Level(user));
    }

    public void save() {

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(xpFile));

            bw.write(xpGainMultiplier);

            bw.newLine();
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

                line = br.readLine();

                String[] partsz = line.split(":");

                try {
                    xpGainMultiplier = Integer.parseInt(partsz[0].trim());
                }catch (NumberFormatException e) {
                    xpGainMultiplier = 3;
                }
                // read file line by line
                while ((line = br.readLine()) != null) {

                    // split the line by :
                    String[] parts = line.split(":");

                    // first part is name, second is number;
                        User user = Main.jda.retrieveUserById(parts[0].trim()).complete();
                        int amountrequiredforlevel = Integer.parseInt(parts[1].trim());
                        int level = Integer.parseInt(parts[2].trim());
                        int xp = Integer.parseInt(parts[3].trim());

                    // put name, number in HashMap if they are
                    // not empty
                    map.put(user, new Level(xp, level, amountrequiredforlevel, user));
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
        if (event.getAuthor().isBot()) return;
        add(event.getAuthor(), xpGainMultiplier);
        if (levels.get(event.getAuthor()).levelUpNeedsToBeProcessed){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.YELLOW);
            builder.addField(" ", event.getAuthor().getName() + " has leveled up to: " + levels.get(event.getAuthor()).level, false);
            builder.setThumbnail(event.getAuthor().getAvatarUrl());
            builder.setAuthor("Selkirk Bot", null, Main.jda.getSelfUser().getAvatarUrl());
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            levels.get(event.getAuthor()).levelUpNeedsToBeProcessed = false;
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("getlevel")){
            if (event.getOption("user") != null) {
                levels.putIfAbsent(event.getOption("user").getAsUser(), new Level(event.getOption("user").getAsUser()));
                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(Color.YELLOW);
                builder.addField("xp", "xp: " + levels.get(event.getOption("user").getAsUser()).xp, false);
                builder.addField("level", "levels: " + levels.get(event.getOption("user").getAsUser()).level, false);
                builder.setThumbnail(event.getUser().getAvatarUrl());
                event.replyEmbeds(builder.build()).queue();
            }else {
                levels.putIfAbsent(event.getUser(), new Level(event.getUser()));
                EmbedBuilder builder = new EmbedBuilder();
                builder.setColor(Color.YELLOW );
                int persentage = levels.get(event.getUser()).xp * 100 / levels.get(event.getUser()).amountRequiredForLevel;
                builder.addField("    ", "xp: " + levels.get(event.getUser()).xp, false);
                builder.addField("   ", "levels: " + levels.get(event.getUser()).level, false);
                builder.addField("  ", "percentage: " + persentage + "%", false);
                builder.setThumbnail(event.getUser().getAvatarUrl());
                event.replyEmbeds(builder.build()).queue();
            }
        }else if (event.getName().equals("leaderboard")){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.YELLOW);
            builder.setTitle("Leaderboard");
            builder.setDescription("the xp leaderboard");
            Map<User, Level> sortedMap = sortByValue(levels);
            int numCount = 1;
            for (Map.Entry<User, Level> entry: sortedMap.entrySet()){
                builder.addField(" ", numCount + ". " + entry.getKey().getName() + " level: " + entry.getValue().level + " xp: " + entry.getValue().xp, false);
                numCount++;
            }
            event.replyEmbeds(builder.build()).queue();
        }
    }

    private static Map<User, Level> sortByValue(Map<User, Level> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<User, Level>> list =
                new LinkedList<Map.Entry<User, Level>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<User, Level>>() {
            public int compare(Map.Entry<User, Level> o1,
                               Map.Entry<User, Level> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<User, Level> sortedMap = new LinkedHashMap<User, Level>();
        for (Map.Entry<User, Level> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }

}
