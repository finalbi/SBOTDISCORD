package org.finalbi.commands;

import net.dv8tion.jda.api.entities.User;

public class MRPSGame {
    public User p1;
    public User p2;
    public boolean accepted = false;
    public boolean finished = false;
    public String p1Awnser;
    public String p2Awnser;

    public MRPSGame(User p1, User p2, String p1Awnser) {
        this.p1 = p1;
        this.p2 = p2;
        this.p1Awnser = p1Awnser;
    }
}
