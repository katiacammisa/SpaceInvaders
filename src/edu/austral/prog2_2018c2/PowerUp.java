package edu.austral.prog2_2018c2;

import java.util.Random;

public class PowerUp {
    String name;

    public PowerUp() {
        int rn = (int) (Math.random() * 10) + 1;
        switch (rn) {
            case 1: name = "Freeze";
            break;
            case 2: case 3: name = "Immunity";
            break;
            case 4: case 5: case 6: case 7: case 8: case 9: case 10: name = "Double Damage";
            break;
        }
    }

    public String getName() {
        return name;
    }
}
