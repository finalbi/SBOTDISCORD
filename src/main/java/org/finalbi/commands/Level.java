package org.finalbi.commands;

import org.finalbi.Main;

import java.io.Serializable;

public class Level {

    public int xp;
    public int level;

    public int amountMultiplyer = 3;

    public int amountRequiredForLevel = 50;

    public boolean levelUpNeedsToBeProcessed = false;

    public Level(int xp, int level, int amountRequiredForLevel) {
        this.xp = xp;
        this.level = level;
        this.amountRequiredForLevel = amountRequiredForLevel;
    }


    public Level(int xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public Level() {
        xp = 0;
        level = 1;
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
}
