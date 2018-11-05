package edu.austral.prog2_2018c2;

import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class ScoreData{

    private String name;
    private int score;

    public ScoreData(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public ScoreData(String thisLine) {
        this.name = thisLine.split(": ")[0];
        this. score = Integer.parseInt(thisLine.split(": ")[1]);
    }

    public String serialize(){
        return name + ": " + score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
