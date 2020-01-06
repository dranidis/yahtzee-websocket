package com.asdt.yahtzee.websocket.messages;

import java.util.Arrays;

public class KeepMessage {
    private String name;
    int[] keep;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getKeep() {
        return keep;
    }

    public void setKeep(int[] keep) {
        this.keep = keep;
    }

    public KeepMessage(String name, int[] keep) {
        this.name = name;
        this.keep = keep;
    }

    public KeepMessage() {
    }

    @Override
    public String toString() {
        return "KeepMessage [keep=" + Arrays.toString(keep) + ", name=" + name + "]";
    }

}
