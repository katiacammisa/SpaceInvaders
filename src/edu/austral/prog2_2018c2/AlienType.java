package edu.austral.prog2_2018c2;

public class AlienType {

   private int points;
   private String image;

   public static AlienType BigAlien = new AlienType(10, "src/images/BigAlienAAAA.png");
   public static AlienType MediumAlien = new AlienType(20, "src/images/MediumAlien.png");
   public static AlienType SmallAlien = new AlienType(30, "src/images/SmallAlien.png");
   public static AlienType UFO = new AlienType((int) (Math.random() * 300) + 50, "src/images/alien.png");

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
