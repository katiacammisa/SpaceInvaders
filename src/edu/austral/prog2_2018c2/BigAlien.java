package edu.austral.prog2_2018c2;

public class BigAlien extends Alien {
    public BigAlien(int x, int y, int life, int points) {
        super(x&(x+1)&(x+2), y&(y+1)&(y+2), life, 10);
    }
}
