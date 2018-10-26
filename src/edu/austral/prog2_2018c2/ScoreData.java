package edu.austral.prog2_2018c2;

public class ScoreData{

    private String name;
    private int score;

    public ScoreData(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String serialize(){
        return name + ": " + score;
    }

    public static ScoreData deserialize(String txt){
        String[] info = txt.split(": ");
        String nam = info[0];
        int scoring = Integer.parseInt(info[1]);
        return new ScoreData(nam, scoring);
    }


}
