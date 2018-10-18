package edu.austral.prog2_2018c2;

import java.util.Random;

public class AlienType {

   private int points;
   private String image;
   private static Random rn = new Random();
   private static int answer = rn.nextInt(300) + 50;

   public static AlienType BigAlien = new AlienType(10, "src/images/BigAlien.png");
   public static AlienType MediumAlien = new AlienType(20, "src/images/MediumAlien.png");
   public static AlienType SmallAlien = new AlienType(30, "src/images/SmallAlien.png");
   public static AlienType UFO = new AlienType(answer, "src/images/UFO.png");

    public AlienType(int points, String image) {
        this.points = points;
        this.image = image;
    }

    public int getPoints() {
        return points;
    }

    public String getImage() {
        return image;
    }
}

