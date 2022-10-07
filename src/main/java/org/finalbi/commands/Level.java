package org.finalbi.commands;

public class Level {

    public int xp;
    public int level;

    public int amountMultiplyer = 3;

    public int amountRequiredForLevel = 50;


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
        level++;
    }

    public void removeXP(int amount){
        xp -= amount;
    }
}
