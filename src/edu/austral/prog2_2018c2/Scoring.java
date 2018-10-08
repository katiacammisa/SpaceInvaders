package edu.austral.prog2_2018c2;

public class Scoring {

    private int score;

    public Scoring() {}

    public void sumPoints(int points)
    {
       score += points;
    }

    public int getScore() {
        return score;
    }
}
