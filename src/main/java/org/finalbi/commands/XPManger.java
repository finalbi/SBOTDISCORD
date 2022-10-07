package org.finalbi.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class XPManger extends ListenerAdapter {

    File xpFile;
    Map<User, Level> levels;
    BufferedWriter br;



    public XPManger() throws IOException {
        levels = new HashMap<>();
        xpFile = new File("xp.txt");
        br = new BufferedWriter(new FileWriter(xpFile));
    }

    public void add(User user, int amount){
        levels.putIfAbsent(user, new Level());
        Level level = levels.get(user);
        level.addXP(amount);
        levels.put(user, level);
    }

    public void save() {

        try {

            // create new BufferedWriter for the output file
            new FileWriter("xp.txt", false).close();
            // iterate map entries
            for (Map.Entry<User, Level
                    > entry :
                    levels.entrySet()) {

                // put key and value separated by a colon
                br.write(entry.getKey() + ":"
                        + entry.getValue());

                // new line
                br.newLine();
            }

            br.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {

                // always close the writer
                br.close();
            }
            catch (Exception e) {
            }
        }
    }

    public void remove(User user, int amount){
        if (levels.containsValue(user)){
            Level level = levels.get(user);
            level.removeXP(amount);
            levels.put(user, level);
        }

    }
}
