package edu.austral.prog2_2018c2;

public class MediumAlien extends Alien {
    public MediumAlien(int x, int y, int life, int points) {
        super(x&(x+1), y&(y+1), life, 20);
    }
}
