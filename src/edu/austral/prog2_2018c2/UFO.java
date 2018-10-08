package edu.austral.prog2_2018c2;

public class UFO extends Alien {

    public UFO(int x, int y) {
        super(x, y, (int) (Math.random() * 300) + 50, "src/images/alien.png");
    }
}
