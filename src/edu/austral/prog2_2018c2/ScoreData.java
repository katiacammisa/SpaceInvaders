package edu.austral.prog2_2018c2;

import java.lang.reflect.Array;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class ScoreData{

    private static String name;
    private static int score;

    public ScoreData(String name, int score) {
        this.name = name;
        this.score = score;
        creanding();
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

    public static ScoreData deserialize(String txt){
        String[] info = txt.split(": ");
        String nam = info[0];
        int scoring = Integer.parseInt(info[1]);
        return new ScoreData(nam, scoring);
    }

    public static ArrayList<String> creanding(){
        ArrayList <String> hS = HighScore.getScoring();
        ArrayList<ScoreData> listeana = new ArrayList<>();

        for (int n = 0; n < 10; n++){
           ScoreData data = deserialize(hS.get(n));
           listeana.set(n, data);
        }
        listeana.add(new ScoreData(name, score));
        ScoreData aux;
        for (int i = 0; i <listeana.size() ; i++) {
            for (int j = 0; j < listeana.size() - i; j++) {
                if(listeana.get(j-1).getScore() > listeana.get(j).getScore()) {
                    aux = listeana.get(j-1);
                    listeana.set(j-1 ,listeana.get(j));
                    listeana.set(j, aux);
                }
            }
        }
        if(listeana.size() > 10) {
            for (int i = 10; i < listeana.size(); i++) {
                listeana.remove(i);
            }
        }
        ArrayList<String> sorted = new ArrayList<String>();
        for (int i = 0; i < listeana.size(); i++) {
            sorted.add(listeana.get(i).serialize());
        }
        return sorted;
    }
}
