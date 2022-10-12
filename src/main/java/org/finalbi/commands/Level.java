package org.finalbi.commands;

import net.dv8tion.jda.api.entities.User;
import org.finalbi.Main;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Level implements Comparable<Level>{

    public int xp;
    public int level;

    public int amountMultiplyer = 3;

    public int amountRequiredForLevel = 50;

    public boolean levelUpNeedsToBeProcessed = false;

    public User user;

    public Level(int xp, int level, int amountRequiredForLevel, User user) {
        this.xp = xp;
        this.level = level;
        this.amountRequiredForLevel = amountRequiredForLevel;
        this.user = user;
    }


    public Level(int xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public Level(User user) {
        xp = 0;
        level = 1;
        this.user = user;
    }

    public void addXP(int amount){
        xp += amount;
        if (xp >= amountRequiredForLevel){
            LevelUp();
        }
    }

    public void LevelUp(){
        amountRequiredForLevel *= amountMultiplyer;
        xp = 0;
        level++;
        levelUpNeedsToBeProcessed = true;
    }

    public void removeXP(int amount){
        xp -= amount;
    }

    @Override
    public int compareTo(@NotNull Level o) {
        if (level > o.level){
            return 1;
        }else if (level == o.level) {
            if (xp > o.xp) {
                return 1;
            } else if (xp == o.xp) {
                return 0;
            } else {
                return -1;
            }

        }

        return -1;
    }
}
