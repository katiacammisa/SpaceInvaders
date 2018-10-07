package edu.austral.prog2_2018c2;

public class UFO extends Alien {

    public UFO(int x, int y, int life, int points) {
        super(x, y, life, (int) (Math.random() * 300) + 50);
    }
}
